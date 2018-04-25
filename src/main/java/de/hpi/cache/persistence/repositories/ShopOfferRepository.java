package de.hpi.cache.persistence.repositories;

import de.hpi.cache.persistence.ShopOffer;

public interface ShopOfferRepository {

    ShopOffer getOffer(long shopId, byte phase);
    void deleteOffer(long shopId, String offerKey);
    void createCollection(long shopId);
    void save(long shopId, ShopOffer shopOffer);
    void deleteAll(long shopId);

}
