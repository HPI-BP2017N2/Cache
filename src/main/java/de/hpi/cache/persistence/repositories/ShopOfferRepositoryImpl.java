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
public class ShopOfferRepositoryImpl implements ShopOfferRepository {

    private final MongoTemplate mongoTemplate;

    // convenience
    @Override
    public ShopOffer getOffer(long shopId, byte phase) {
        ShopOffer idealoOffer = getMongoTemplate()
                .findOne(query(where("phase").is(phase)), ShopOffer.class , Long.toString(shopId));
        if(idealoOffer != null) {
            getMongoTemplate()
                    .updateFirst(query(where("_id")
                            .is(idealoOffer.getOfferKey())), update("phase", phase + 1), Long.toString(shopId));
        }
        return idealoOffer;
    }

    @Override
    public void deleteOffer(long shopId, String offerKey) {
        getMongoTemplate().remove(query(where("offerKey").is(offerKey)), Long.toString(shopId));
    }


    @Override
    public void save(long shopId, ShopOffer shopOffer) {

        getMongoTemplate().insert(shopOffer, Long.toString(shopId));
    }

    @Override
    public void deleteAll(long shopId) {
        getMongoTemplate().dropCollection(Long.toString(shopId));
    }

    @Override
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