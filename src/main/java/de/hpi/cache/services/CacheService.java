package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.OfferList;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CacheService {

    private IdealoBridge idealoBridge;

    private ShopOfferRepositoryImpl repository;

    @Autowired
    public CacheService(IdealoBridge idealoBridge, ShopOfferRepositoryImpl shopOfferRepository) {
        setIdealoBridge(idealoBridge);
        setRepository(shopOfferRepository);
    }

    public IdealoOffer getOffer(long shopId, short phase) {
        return getRepository().getOffer(shopId, phase);
    }

    public void deleteOffer(long shopId, String offerKey) {
        getRepository().deleteOffer(shopId, offerKey);
    }

    public void deleteAll(long shopId) {
        getRepository().deleteAll(shopId);
    }

    public void warmup(long shopId){
        OfferList offerList =  getIdealoBridge().getOffers(shopId);
        System.out.println(offerList.getFirst().getOfferKey().get("value"));
    }


}
