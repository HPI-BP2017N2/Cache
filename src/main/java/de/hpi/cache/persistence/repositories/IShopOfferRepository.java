package de.hpi.cache.persistence.repositories;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.persistence.ShopOffer;

public interface IShopOfferRepository {

    IdealoOffer getOffer(long shopId, short phase);
    void deleteOffer(long shopId, String offerKey);
    void createCollection(long shopId);
    void save(long shopId, ShopOffer shopOffer);
    void deleteAll(long shopId);

}
