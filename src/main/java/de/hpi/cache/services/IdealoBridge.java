package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties properties;

    public String getOffers(long shopID) {
        String inputStream = (getOAuthRestTemplate().getForObject(getOffersURI(shopID), String.class));
        System.out.println(inputStream);
        InputStream stream = new ByteArrayInputStream(inputStream.getBytes(StandardCharsets.UTF_8));
        try {
            processStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
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