package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.IdealoOfferList;
import de.hpi.cache.dto.Property;
import de.hpi.cache.dto.ShopIDToRootUrlResponse;
import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepository;
import de.hpi.cache.persistence.repositories.UrlCleaner;
import de.hpi.cache.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.ws.http.HTTPException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties properties;

    private final UrlCleaner urlCleaner;

    private static final Logger logger = LogManager.getLogger(IdealoBridge.class);

    private final ShopOfferRepository repository;


    @Retryable(
            value = {HttpClientErrorException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    public void getOffers(long shopId) {
        logger.info("Start fetching shop {}", shopId);
        IdealoOfferList offers = getOAuthRestTemplate().getForObject(getOffersURI(shopId), IdealoOfferList.class);
        logger.debug("Fetched shop {}.", shopId);
        logger.debug("Start writing offers of {}.", shopId);

        String rootUrl = getRootUrl(shopId);

        List<Integer> imageUrlsIdPosition = PictureIdFinder.findPictureId(offers.subList(0, Math.min(100, offers.size())));
        for (IdealoOffer offer : offers) {
            ShopOffer shopOffer = offer.toShopOffer();
            Map<String, String> urls = new HashMap<>();
            String key = offer.getUrls().getValue().keySet().iterator().next();
            urls.put(key, getCleanedUrl(offer.getShopId().getValue(), offer.getUrls().getValue().get(key), rootUrl));

            shopOffer.setUrls(urls);

            Property<Map<String, List<String>>> imageUrls = offer.getImageUrls();
            if (imageUrls != null) {
                String imageUrl = imageUrls.getValue().get(imageUrls.getValue().keySet().iterator().next()).get(0);
                String[] urlParts = PictureIdFinder.splitUrl(imageUrl);
                String uniqueParts = "";
                for (int position : imageUrlsIdPosition) {
                    uniqueParts = uniqueParts.concat(urlParts[position]);
                }
                shopOffer.setImageId(uniqueParts);
            }
            getRepository().save(shopId, shopOffer);
        }

        logger.info("Wrote {} offers of {}.", offers.size(), shopId);

        offers = null;
        System.gc();
    }

    @Retryable(
            value = {HttpClientErrorException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))

    private String getRootUrl(long shopId) {
        return getOAuthRestTemplate().getForObject(getRootUrlURI(shopId), ShopIDToRootUrlResponse.class).getShopUrl();
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }

    private URI getRootUrlURI(long shopId) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getRootUrlRoute() + shopId)
                .build()
                .encode()
                .toUri();
    }

    private String getCleanedUrl(long shopId, String dirtyUrl, String rootUrl) {
        try {
            return  getUrlCleaner().cleanUrl(dirtyUrl, shopId, rootUrl);
        } catch (HTTPException e){
            return null;
        }
    }

}
