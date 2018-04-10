package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.OfferList;
import de.hpi.cache.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class IdealoBridge {

    private final RestTemplate oAuthRestTemplate;

    private final IdealoBridgeProperties properties;

    public String getOffers(long shopID) {
        System.out.println(getOAuthRestTemplate().getForObject(getOffersURI(shopID), OfferList.class).next());

        //Iterator b = getOAuthRestTemplate().get
        //System.out.println(a.hasNext());
        //return getOAuthRestTemplate().getForObject(getOffersURI(shopID), ShopIDToRootUrlResponse
                //.class).getShopUrl();
        return null;
    }

    private URI getOffersURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getProperties().getApiUrl())
                .path(getProperties().getOfferRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }
}