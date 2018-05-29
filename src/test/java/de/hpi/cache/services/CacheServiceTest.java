package de.hpi.cache.services;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepository;
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
    @Mock private ShopOfferRepository repository;

    private CacheService service;

    @Before
    public void setup(){
        setService(new CacheService(getBridge(), getRepository()));
    }

    @Test
    public void getOfferByOfferKey(){
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getRepository()).getOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        ShopOffer offer = getService().getOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        verify(getRepository()).getOffer(eq(getEXAMPLE_SHOP_ID()), eq(getEXAMPLE_OFFER_KEY()));
        assertEquals(getEXAMPlE_SHOP_OFFER(), offer);

    }

    @Test
    public void getOfferByPhase(){
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getRepository()).getOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());

        ShopOffer offer = getService().getOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());

        verify(getRepository()).getOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());
        assertEquals(getEXAMPlE_SHOP_OFFER(), offer);
    }

    @Test
    public void markOfferAsMatched(){
        doNothing().when(getRepository()).markAsMatched(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getService().markAsMatched(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        verify(getRepository()).markAsMatched(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());
    }

    @Test
    public void getUnmatchedOfferAndUpdatePhase() {
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getRepository()).getUnmatchedOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());

        ShopOffer offer = getService().getUnmatchedOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());

        verify(getRepository()).getUnmatchedOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());
        assertEquals(getEXAMPlE_SHOP_OFFER(), offer);
    }

    @Test
    public void updatePhase(){
        doNothing().when(getRepository()).updatePhase(getEXAMPLE_SHOP_ID(), getPHASE(), getPHASE());

        getService().updatePhase(getEXAMPLE_SHOP_ID(), getPHASE(), getPHASE());

        verify(getRepository()).updatePhase(getEXAMPLE_SHOP_ID(), getPHASE(), getPHASE());
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