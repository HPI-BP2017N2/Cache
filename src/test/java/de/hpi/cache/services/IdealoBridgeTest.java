package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.IdealoOfferList;
import de.hpi.cache.dto.Property;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class IdealoBridgeTest {

    @Getter(AccessLevel.PRIVATE) private static final long EXAMPLE_SHOP_ID = 1234;
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_CATEGORY = "12345";
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_API_URL = "http://api.example.com/";
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_OFFER_ROUTE = "offers/";

    private final IdealoOfferList idealoOffers = new IdealoOfferList();
    private final ShopOffer expectedShopOffer = new ShopOffer();
    private IdealoBridge bridge;

    @Mock private RestTemplate restTemplate;
    @Mock private IdealoBridgeProperties properties;
    @Mock private ShopOfferRepository repository;

    @Before
    public void setup() {
        initMocks(this);
        IdealoOffer idealoOffer = new IdealoOffer();

        Property<Long> shopId = new Property<>();
        Property<String> category = new Property<>();
        shopId.setValue(getEXAMPLE_SHOP_ID());
        category.setValue(getEXAMPLE_CATEGORY());

        idealoOffer.setShopId(shopId);
        idealoOffer.setMappedCatalogCategory(category);

        getExpectedShopOffer().setShopId(getEXAMPLE_SHOP_ID());
        getExpectedShopOffer().setMappedCatalogCategory(getEXAMPLE_CATEGORY());

        getIdealoOffers().add(idealoOffer);

        setBridge(new IdealoBridge(
                getRestTemplate(),
                getProperties(),
                getRepository()
        ));

    }

    @Test
    public void getOffers() {
        doReturn(getIdealoOffers()).when(getRestTemplate()).getForObject(any(URI.class), eq(IdealoOfferList.class));
        doReturn(getEXAMPLE_API_URL()).when(getProperties()).getApiUrl();
        doReturn(getEXAMPLE_OFFER_ROUTE()).when(getProperties()).getOfferRoute();
        getBridge().getOffers(getEXAMPLE_SHOP_ID());
        verify(getRepository(), times(getIdealoOffers().size())).save(getEXAMPLE_SHOP_ID(), getExpectedShopOffer());

    }
}