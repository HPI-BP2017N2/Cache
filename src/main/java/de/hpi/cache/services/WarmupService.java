package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import lombok.AccessLevel;
import lombok.Getter;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@Getter(AccessLevel.PRIVATE)
public class WarmupService {
     @Autowired
     private IdealoBridge idealoBridge;
     @Autowired
     private ShopOfferRepositoryImpl repository;

     public void warmup(long shopId){
         InputStream stream =  getIdealoBridge().getOffers(shopId);

         try {
             processStream(stream);
         } catch (IOException e) {
             e.printStackTrace();
         }

     }

    private void processStream(InputStream inputStream) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonFactory jsonFactory = objectMapper.getJsonFactory();
        try (final JsonParser jsonParser = jsonFactory.createJsonParser(inputStream)) {
            final JsonToken arrayToken = jsonParser.nextToken();
            if (arrayToken == null) {

                return;
            }

            if (!JsonToken.START_ARRAY.equals(arrayToken)) {

                return;
            }

            // Iterate through the objects of the array.
            while (JsonToken.START_OBJECT.equals(jsonParser.nextToken())) {
                IdealoOffer responseData = jsonParser.readValueAs(IdealoOffer.class);
                System.out.println(responseData);
            }
        }

    }

}
