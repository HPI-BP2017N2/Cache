package de.hpi.cache.persistence.repositories;

import de.hpi.cache.persistence.ShopOffer;

public interface IShopOfferRepository {

    ShopOffer getOffer(long shopId, short phase);
    void deleteOffer(String offerKey);
    void save(long shopId, ShopOffer shopOffer);
    void delete(long shopId);

}
