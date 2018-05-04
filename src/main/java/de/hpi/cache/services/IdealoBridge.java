package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.IdealoOfferList;
import de.hpi.cache.persistence.repositories.ShopOfferRepository;
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

import java.net.URI;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties properties;

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

        for (IdealoOffer offer : offers) {
            getRepository().save(shopId, offer.toShopOffer());
        }

        logger.info("Wrote {} offers of {}.", offers.size(), shopId);

        offers = null;
        System.gc();
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }

}