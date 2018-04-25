package de.hpi.cache.api;

import de.hpi.cache.persistence.ShopOffer;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    public void getExistingOffer() throws Exception {
        doReturn(getEXAMPlE_SHOP_OFFER()).when(getCacheService()).getOffer(getEXAMPLE_SHOP_ID(), getPHASE());

        getMockMvc()
                .perform(get("/getOffer/" + getEXAMPLE_SHOP_ID()).param("phase", Long.toString(getPHASE())))
                .andExpect(status().isOk());

        verify(getCacheService()).getOffer(getEXAMPLE_SHOP_ID(), getPHASE());
    }

    @Test
    public void deleteOffer() throws Exception {
        doNothing().when(getCacheService()).deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getMockMvc()
                .perform(delete("/deleteOffer/" + getEXAMPLE_SHOP_ID()).param("offerKey", getEXAMPLE_OFFER_KEY()))
                .andExpect(status().isOk());

        verify(getCacheService()).deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());
    }

    @Test
    public void deleteAll() throws Exception {
        doNothing().when(getCacheService()).deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getMockMvc()
                .perform(delete("/deleteAll/" + getEXAMPLE_SHOP_ID()))
                .andExpect(status().isOk());

        verify(getCacheService()).deleteAll(getEXAMPLE_SHOP_ID());
    }

    @Test
    public void warmup() throws Exception {
        doNothing().when(getCacheService()).deleteOffer(getEXAMPLE_SHOP_ID(), getEXAMPLE_OFFER_KEY());

        getMockMvc()
                .perform(get("/warmup/" + getEXAMPLE_SHOP_ID()))
                .andExpect(status().isOk());

        verify(getCacheService()).warmup(getEXAMPLE_SHOP_ID());
    }
}