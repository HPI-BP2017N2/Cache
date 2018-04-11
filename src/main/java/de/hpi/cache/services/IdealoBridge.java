package de.hpi.cache.services;

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

    public InputStream getOffers(long shopID) {
        String inputStream = (getOAuthRestTemplate().getForObject(getOffersURI(shopID), String.class));
        InputStream stream = new ByteArrayInputStream(inputStream.getBytes(StandardCharsets.UTF_8));
        return stream;
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }

}