package de.hpi.cache.services;

import de.hpi.cache.dto.*;
import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepository;
import de.hpi.cache.persistence.repositories.UrlCleaner;
import de.hpi.cache.properties.CacheProperties;
import de.hpi.cache.properties.IdealoBridgeProperties;
import javafx.util.Pair;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.ws.http.HTTPException;
import java.net.URI;
import java.util.*;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties bridgeProperties;

    private final CacheProperties cacheProperties;

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
        int wantedCategoryLevel = getCacheProperties().getCategoryMappingLevel();

        for (IdealoOffer offer : offers) {
            ShopOffer shopOffer = offer.toShopOffer();
            shopOffer.setUrls(getCleanedUrls(shopId, rootUrl, offer.getUrls().getValue()));

            if(offer.getMappedCatalogCategory() != null) {
                Pair<IdealoCategory, IdealoCategory> categories = getActualAndHigherLevelCategory(offer.getMappedCatalogCategory().getValue(), wantedCategoryLevel);
                shopOffer.setCategoryName(categories.getKey().getCategoryName());
                shopOffer.setHigherLevelCategory(categories.getValue().getCategoryId());
                shopOffer.setHigherLevelCategoryName(categories.getValue().getCategoryName());
            }

            Property<Map<String, List<String>>> imageUrls = offer.getImageUrls();
            if (imageUrls != null) {
                String imageUrl = imageUrls.getValue().get(imageUrls.getValue().keySet().iterator().next()).get(0);
                shopOffer.setImageId(PictureIdFinder.getImageId(imageUrl, imageUrlsIdPosition));
            }
            getRepository().save(shopId, shopOffer);
        }
        logger.info("Wrote {} offers of {}.", offers.size(), shopId);
        offers = null;
        System.gc();
    }

    private Pair<IdealoCategory, IdealoCategory> getActualAndHigherLevelCategory(String categoryId, int wantedCategoryLevel) {
            IdealoCategory higherLevelCategory;
            List<IdealoCategory> parentCategories = getCategoryPath(categoryId);

            if(parentCategories.size() < wantedCategoryLevel) {
                higherLevelCategory = parentCategories.get(parentCategories.size() - 1);
            } else {
                higherLevelCategory = parentCategories.get(parentCategories.size() - Math.max(wantedCategoryLevel, 1));
            }
            return new Pair<>(parentCategories.get(0), higherLevelCategory);
    }

    private List<IdealoCategory> getCategoryPath(String categoryId) {
        String currentCategoryId = categoryId;
        List<IdealoCategory> categories = new ArrayList<>();

        do {
            IdealoCategory currentCategory = getCategoryInformation(currentCategoryId);
            categories.add(currentCategory);
            currentCategoryId = currentCategory.getParentCategoryId();
        } while(!currentCategoryId.equals("100"));

        return categories;
    }

    private Map<String, String> getCleanedUrls(long shopId, String rootUrl, Map<String, String> urls) {
        Map<String, String> cleanedUrls = new HashMap<>();
        String key = urls.keySet().iterator().next();
        urls.put(key, getCleanedUrl(shopId, urls.get(key), rootUrl));
        return cleanedUrls;
    }

    @Retryable(
            value = {HttpClientErrorException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    private String getRootUrl(long shopId) {
        return getOAuthRestTemplate().getForObject(getRootUrlURI(shopId), ShopIDToRootUrlResponse.class).getShopUrl();
    }

    @Retryable(
            value = {HttpClientErrorException.class, ResourceAccessException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    private IdealoCategory getCategoryInformation(String categoryId) {
        return getOAuthRestTemplate().getForObject(getCategoryURI(categoryId), IdealoCategory.class);
    }

    private String getCleanedUrl(long shopId, String dirtyUrl, String rootUrl) {
        try {
            return  getUrlCleaner().cleanUrl(dirtyUrl, shopId, rootUrl);
        } catch (HTTPException e){
            return dirtyUrl;
        } catch (ResourceAccessException e) {
            return dirtyUrl;
        }

    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getBridgeProperties().getApiUrl())
                .path(getBridgeProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }

    private URI getRootUrlURI(long shopId) {
        return UriComponentsBuilder.fromUriString(getBridgeProperties().getApiUrl())
                .path(getBridgeProperties().getRootUrlRoute() + shopId)
                .build()
                .encode()
                .toUri();
    }

    private URI getCategoryURI(String categoryId) {
        return UriComponentsBuilder.fromUriString(getBridgeProperties().getApiUrl())
                .path(getBridgeProperties().getCategoryRoute() + categoryId)
                .build()
                .encode()
                .toUri();
    }

}
