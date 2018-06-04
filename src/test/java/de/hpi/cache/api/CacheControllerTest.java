package de.hpi.cache.api;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.WarmingUpShops;
import de.hpi.cache.services.CacheService;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@Getter(AccessLevel.PRIVATE)
public class CacheControllerTest {

    @Getter(AccessLevel.PRIVATE) private static final byte PHASE = 0;
    @Getter(AccessLevel.PRIVATE) private static final long EXAMPLE_SHOP_ID = 1234;
    @Getter(AccessLevel.PRIVATE) private static final String EXAMPLE_OFFER_KEY = "abc";
    @Getter(AccessLevel.PRIVATE) private static final ShopOffer EXAMPlE_SHOP_OFFER = new ShopOffer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheService cacheService;

    @Test
    public void getExistingOfferByKey() throws Exception {
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getCacheService()).getOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getMockMvc()
                .perform(get("/getOffer/" + getEXAMPLE_SHOP_ID()).param("offerKey", getEXAMPLE_OFFER_KEY()))
                .andExpect(status().isOk());

        verify(getCacheService()).getOffer(eq(getEXAMPLE_SHOP_ID()), eq(getEXAMPLE_OFFER_KEY()));
    }

    @Test
    public void getExistingOfferByPhase() throws Exception {
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getCacheService()).getOfferAndUpdatePhase(eq(getEXAMPLE_SHOP_ID()), eq(getPHASE()), any(WarmingUpShops.class));

        getMockMvc()
                .perform(get("/getOfferAndUpdatePhase/" + getEXAMPLE_SHOP_ID()).param("phase", Byte.toString(getPHASE())))
                .andExpect(status().isOk());

        verify(getCacheService()).getOfferAndUpdatePhase(eq(getEXAMPLE_SHOP_ID()), eq(getPHASE()), any(WarmingUpShops.class));
    }

    @Test
    public void deleteOffer() throws Exception {
        doNothing().when(getCacheService()).markAsMatched(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getMockMvc()
                .perform(delete("/markAsMatched/" + getEXAMPLE_SHOP_ID()).param("offerKey", getEXAMPLE_OFFER_KEY()))
                .andExpect(status().isOk());

        verify(getCacheService()).markAsMatched(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());
    }

    @Test
    public void deleteAll() throws Exception {
        doNothing().when(getCacheService()).markAsMatched(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getMockMvc()
                .perform(delete("/deleteAll/" + getEXAMPLE_SHOP_ID()))
                .andExpect(status().isOk());

        verify(getCacheService()).deleteAll(getEXAMPLE_SHOP_ID());
    }

    @Test
    public void warmup() throws Exception {
        doNothing().when(getCacheService()).warmup(eq(getEXAMPLE_SHOP_ID()), any(WarmingUpShops.class));

        getMockMvc()
                .perform(get("/warmup/" + getEXAMPLE_SHOP_ID()))
                .andExpect(status().isOk());

        verify(getCacheService()).warmup(eq(getEXAMPLE_SHOP_ID()), any(WarmingUpShops.class));
    }

    @Test
    public void getUnmatchedOfferAndUpdatePhase() throws Exception {
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getCacheService()).getUnmatchedOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());

        getMockMvc()
                .perform(get("/getUnmatchedOfferAndUpdatePhase/" + getEXAMPLE_SHOP_ID()).param("phase", Byte.toString(getPHASE())))
                .andExpect(status().isOk());

        verify(getCacheService()).getUnmatchedOfferAndUpdatePhase(getEXAMPLE_SHOP_ID(), getPHASE());
    }

    @Test
    public void updatePhase() throws Exception {
        doNothing().when(getCacheService()).updatePhase(getEXAMPLE_SHOP_ID(), getPHASE(), getPHASE());

        getMockMvc()
                .perform(post("/updatePhase/" + getEXAMPLE_SHOP_ID()).param("oldPhase", Byte.toString(getPHASE())).param("newPhase", Byte.toString(getPHASE())))
                .andExpect(status().isOk());

        verify(getCacheService()).updatePhase(getEXAMPLE_SHOP_ID(), getPHASE(), getPHASE());

    }
}