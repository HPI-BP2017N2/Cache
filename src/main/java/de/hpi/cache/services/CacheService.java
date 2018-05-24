package de.hpi.cache.services;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CacheService {

    private final IdealoBridge idealoBridge;

    private final ShopOfferRepositoryImpl repository;

    private static final Logger logger = LogManager.getLogger(CacheService.class);


    public ShopOffer getOffer(long shopId, String offerKey) {
        return getRepository().getOffer(shopId, offerKey);
    }

    public ShopOffer getOfferAndUpdatePhase(long shopId, byte phase) {
        return getRepository().getOfferAndUpdatePhase(shopId, phase);

    }

    public void markAsMatched(long shopId, String offerKey) {
        getRepository().markAsMatched(shopId, offerKey);
    }

    public void deleteAll(long shopId) {
        getRepository().deleteAll(shopId);
    }

    public void warmup(long shopId){
        deleteAll(shopId);
        getRepository().createCollection(shopId);
        getIdealoBridge().getOffers(shopId);
    }

}
