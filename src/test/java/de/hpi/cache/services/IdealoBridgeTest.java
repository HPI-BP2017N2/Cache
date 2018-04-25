package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.IdealoOfferList;
import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepository;
import de.hpi.cache.properties.IdealoBridgeProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class IdealoBridgeTest {

    @Getter(AccessLevel.PRIVATE) private static final long EXAMPLE_SHOP_ID = 1234;
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_API_URL = "http://api.example.com/";
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_OFFER_ROUTE = "offers/";

    private final IdealoOfferList idealoOffers = new IdealoOfferList();
    private IdealoBridge bridge;

    @Mock private RestTemplate restTemplate;
    @Mock private IdealoBridgeProperties properties;
    @Mock private ShopOfferRepository repository;

    @Before
    public void setup() {
        initMocks(this);
        IdealoOffer idealoOffer = new IdealoOffer();

        getIdealoOffers().add(idealoOffer);

        setBridge(new IdealoBridge(
                getRestTemplate(),
                getProperties(),
                getRepository()
        ));

    }
    @Test
    public void getOffers() {
        doReturn(getIdealoOffers()).when(getRestTemplate()).getForObject(any(URI.class), any(Class.class));
        doReturn(getEXAMPLE_API_URL()).when(getProperties()).getApiUrl();
        doReturn(getEXAMPLE_OFFER_ROUTE()).when(getProperties()).getOfferRoute();
        getBridge().getOffers(getEXAMPLE_SHOP_ID());
        verify(getRepository(), times(getIdealoOffers().size())).save(anyLong(), any(ShopOffer.class));

    }
}