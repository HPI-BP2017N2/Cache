package de.hpi.cache.services;

import de.hpi.cache.dto.OfferList;
import de.hpi.cache.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
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

    public OfferList getOffers(long shopID) {
        //String test = (getOAuthRestTemplate().getForObject(getOffersURI(shopID), String.class));
        //System.out.println(test.substring(0, 100));
        //return null;
        return getOAuthRestTemplate().getForObject(getOffersURI(shopID), OfferList.class);
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }

}