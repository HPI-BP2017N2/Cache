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


    /**
     *
     * @param shopId The ID of the shop
     * @param offerKey The ID of the offer
     * @return The offer specified by shopId and offerKey
     */
    public ShopOffer getOffer(long shopId, String offerKey) {
        return getRepository().getOffer(shopId, offerKey);
    }

    /**
     * This method finds the first offer for the specified shop with the requested phase and the matched flag set to false.
     * It also increases the phase of the offer.
     * @param shopId The ID of the shop
     * @param phase The matching phase the offer should be in
     * @return The first offer of the shop in the requested phase that was not already matched
     */
    public ShopOffer getUnmatchedOfferAndUpdatePhase(long shopId, byte phase) {
        return getRepository().getUnmatchedOfferAndUpdatePhase(shopId, phase);
    }

    /**
     * This method finds the first offer for the specified shop with the requested phase.
     * It also increases the phase of the offer.
     * @param shopId The ID of the shop
     * @param phase The matching phase the offer should be in
     * @param currentlyWarmingUp Mutex to avoid multiple warming up of same shop. Used to make sure requested shop is already downloaded.
     * @return The first offer of the shop in the requested phase
     */
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

    /**
     * This method sets the phase of all offers of the requested shop with specified phase to the new one.
     * @param shopId The ID of the shop
     * @param oldPhase The current phase
     * @param newPhase The new phase
     */
    public void updatePhase(long shopId, byte oldPhase, byte newPhase) {
        getRepository().updatePhase(shopId, oldPhase, newPhase);
    }

    /**
     * This method deletes all offers of one shop.
     * @param shopId The ID of the shop
     */
    public void deleteAll(long shopId) {
        getRepository().deleteAll(shopId);
    }

    /**
     * This method starts downloading and preparing all offers for one shop.
     * For every offer, it will<br />
     * 1. add category information (actual category and higher level category),<br />
     * 2. clean the URL and <br />
     * 3. extract unique parts of image URL (image ID).
     * @param shopId The ID of the shop
     * @param currentlyWarmingUp Mutex to avoid multiple warming up of same shop.
     */
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
