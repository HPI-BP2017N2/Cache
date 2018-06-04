package de.hpi.cache.services;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.WarmingUpShops;
import de.hpi.cache.persistence.repositories.ShopOfferRepository;
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

    private final ShopOfferRepository repository;

    private static final Logger logger = LogManager.getLogger(CacheService.class);


    public ShopOffer getOffer(long shopId, String offerKey) {
        return getRepository().getOffer(shopId, offerKey);
    }

    public ShopOffer getUnmatchedOfferAndUpdatePhase(long shopId, byte phase) {
        return getRepository().getUnmatchedOfferAndUpdatePhase(shopId, phase);
    }

    public ShopOffer getOfferAndUpdatePhase(long shopId, byte phase, WarmingUpShops currentlyWarmingUp) {
        while(currentlyWarmingUp.isWarmingUp(shopId)) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return getRepository().getOfferAndUpdatePhase(shopId, phase);
    }

    public void markAsMatched(long shopId, String offerKey) {
        getRepository().markAsMatched(shopId, offerKey);
    }

    public void updatePhase(long shopId, byte oldPhase, byte newPhase) {
        getRepository().updatePhase(shopId, oldPhase, newPhase);
    }

    public void deleteAll(long shopId) {
        getRepository().deleteAll(shopId);
    }

    public void warmup(long shopId, WarmingUpShops currentlyWarmingUp){
        if(currentlyWarmingUp.isWarmingUp(shopId)) {
            return;
        }
        deleteAll(shopId);
        getRepository().createCollection(shopId);

        Thread warmUpThread = new Thread(() -> getIdealoBridge().getOffers(shopId, currentlyWarmingUp));
        warmUpThread.start();
    }

}
