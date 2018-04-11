package de.hpi.cache.persistence.repositories;

import com.mongodb.client.MongoCollection;
import de.hpi.cache.persistence.ShopOffer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Getter(AccessLevel.PRIVATE)
public class ShopOfferRepositoryImpl implements IShopOfferRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ShopOffer getOffer(long shopId, short phase) {
        return null;
    }

    @Override
    public void deleteOffer(String offerKey) {

    }

    @Override
    public void save(long shopId, ShopOffer shopOffer) {
        if(!collectionExists(shopId)) {
            createCollection(shopId);
        }
        MongoCollection collection = getMongoTemplate().getCollection(Long.toString(shopId));
        collection.insertOne(convertShopOfferToDbObject(shopOffer));
    }

    @Override
    public void delete(long shopId) {

    }

    // conversion
    private Bson convertShopOfferToDbObject(ShopOffer shopOffer){
        Bson bson = new Document();
        getMongoTemplate().getConverter().write(shopOffer, bson);
        return bson;
    }

    // conditionals
    private boolean collectionExists(long shopId){
        return getMongoTemplate().collectionExists(Long.toString(shopId));
    }

    private void createCollection(long shopId){
        getMongoTemplate().createCollection(Long.toString(shopId));
    }

}
