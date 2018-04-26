package de.hpi.cache.services;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CacheServiceTest {

    @Getter(AccessLevel.PRIVATE) private static final byte PHASE = 0;
    @Getter(AccessLevel.PRIVATE) private static final long EXAMPLE_SHOP_ID = 1234;
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_OFFER_KEY = "abc";
    @Getter(AccessLevel.PRIVATE) private static final ShopOffer EXAMPlE_SHOP_OFFER = new ShopOffer();

    @Mock private IdealoBridge bridge;
    @Mock private ShopOfferRepositoryImpl repository;

    private CacheService service;

    @Before
    public void setup(){
        setService(new CacheService(getBridge(), getRepository()));
    }

    @Test
    public void getOffer(){
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getRepository()).getOffer(getEXAMPLE_SHOP_ID(), getPHASE());

        ShopOffer offer = getService().getOffer(getEXAMPLE_SHOP_ID(), getPHASE());

        verify(getRepository()).getOffer(getEXAMPLE_SHOP_ID(), getPHASE());
        assertEquals(getEXAMPlE_SHOP_OFFER(), offer);
    }

    @Test
    public void deleteOffer(){
        doNothing().when(getRepository()).deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getService().deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        verify(getRepository()).deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());
    }

    @Test
    public void deleteAll(){
        doNothing().when(getRepository()).deleteAll(getEXAMPLE_SHOP_ID());

        getService().deleteAll(getEXAMPLE_SHOP_ID());

        verify(getRepository()).deleteAll(getEXAMPLE_SHOP_ID());
    }

    @Test
    public void warmup(){
        doNothing().when(getRepository()).deleteAll(getEXAMPLE_SHOP_ID());
        doNothing().when(getRepository()).createCollection(getEXAMPLE_SHOP_ID());

        getService().warmup(getEXAMPLE_SHOP_ID());

        verify(getRepository()).deleteAll(getEXAMPLE_SHOP_ID());
        verify(getRepository()).createCollection(getEXAMPLE_SHOP_ID());
    }

}