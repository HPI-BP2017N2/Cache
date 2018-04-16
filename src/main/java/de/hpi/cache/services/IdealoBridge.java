package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import de.hpi.cache.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties properties;

    private static final Logger logger = LogManager.getLogger(IdealoBridge.class);

    @Autowired
    private ShopOfferRepositoryImpl repository;

    public void getOffers(long shopID) {
        String stream = getOAuthRestTemplate().getForObject(getOffersURI(shopID), String.class);
        processStream(new ByteArrayInputStream(stream.getBytes()), shopID);
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }

    private void processStream(InputStream inputStream, long shopId) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonFactory jsonFactory = objectMapper.getJsonFactory();
        try (final JsonParser jsonParser = jsonFactory.createJsonParser(inputStream)) {
            final JsonToken arrayToken = jsonParser.nextToken();
            if (arrayToken == null) {
                logger.error("No shop data received for shop {}", shopId);
                return;
            }

            if (!JsonToken.START_ARRAY.equals(arrayToken)) {
                logger.error("Malformed shop data received for shop {}", shopId);
                return;
            }

            while (JsonToken.START_OBJECT.equals(jsonParser.nextToken())) {
                getRepository().save(shopId, jsonParser.readValueAs(IdealoOffer.class).toShopOffer());
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}