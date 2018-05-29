package de.hpi.cache.persistence.repositories;

import de.hpi.cache.persistence.ShopOffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Repository
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ShopOfferRepository {

    private final MongoTemplate mongoTemplate;

    public ShopOffer getOffer(long shopId, String offerKey) {
        return getMongoTemplate().findOne(query(where("offerKey").is(offerKey)), ShopOffer.class, Long.toString(shopId));
    }

    // convenience

    public ShopOffer getOfferAndUpdatePhase(long shopId, byte phase) {
        ShopOffer idealoOffer = getMongoTemplate()
                .findOne(query(where("phase").is(phase)), ShopOffer.class , Long.toString(shopId));
        if(idealoOffer != null) {
            getMongoTemplate()
                    .updateFirst(query(where("_id")
                            .is(idealoOffer.getOfferKey())), update("phase", phase + 1), Long.toString(shopId));
        }
        return idealoOffer;
    }

    public ShopOffer getUnmatchedOfferAndUpdatePhase(long shopId, byte phase) {
        ShopOffer idealoOffer = getMongoTemplate()
                .findOne(query(where("phase").is(phase).and("isMatched").is(false)), ShopOffer.class , Long.toString(shopId));
        if(idealoOffer != null) {
            getMongoTemplate()
                    .updateFirst(query(where("_id")
                            .is(idealoOffer.getOfferKey())), update("phase", phase + 1), Long.toString(shopId));
        }
        return idealoOffer;

    }

    public void updatePhase(long shopId, byte oldPhase, byte newPhase) {
        getMongoTemplate().updateMulti(query(where("phase").is(oldPhase)), update("phase", newPhase), ShopOffer.class, Long.toString(shopId));
    }


    public void markAsMatched(long shopId, String offerKey) {
            getMongoTemplate()
                    .updateFirst(query(where("_id")
                            .is(offerKey)), update("isMatched", true), Long.toString(shopId));
    }



    public void save(long shopId, ShopOffer shopOffer) {
        getMongoTemplate().insert(shopOffer, Long.toString(shopId));
    }

    public void deleteAll(long shopId) {
        getMongoTemplate().dropCollection(Long.toString(shopId));
    }

    public void createCollection(long shopId){
        if(!collectionExists(shopId)) {
        getMongoTemplate().createCollection(Long.toString(shopId));
        }
    }

    // conditionals
    private boolean collectionExists(long shopId){
        return getMongoTemplate().collectionExists(Long.toString(shopId));
    }

}