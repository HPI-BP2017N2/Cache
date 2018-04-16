package de.hpi.cache.services;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CacheService {

    private IdealoBridge idealoBridge;

    private ShopOfferRepositoryImpl repository;

    private static final Logger logger = LogManager.getLogger(CacheService.class);


    @Autowired
    public CacheService(IdealoBridge idealoBridge, ShopOfferRepositoryImpl shopOfferRepository) {
        setIdealoBridge(idealoBridge);
        setRepository(shopOfferRepository);
    }

    public ShopOffer getOffer(long shopId, byte phase) {
        return getRepository().getOffer(shopId, phase);
    }

    public void deleteOffer(long shopId, String offerKey) {
        getRepository().deleteOffer(shopId, offerKey);
    }

    public void deleteAll(long shopId) {
        getRepository().deleteAll(shopId);
    }

    public void warmup(long shopId){
        deleteAll(shopId);
        logger.debug("Started fetching shop {}", shopId);
        getRepository().createCollection(shopId);
        getIdealoBridge().getOffers(shopId);
        logger.debug("Fetched shop {}.", shopId);
    }


}
