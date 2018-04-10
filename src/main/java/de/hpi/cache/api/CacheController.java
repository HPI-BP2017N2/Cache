package de.hpi.cache.api;

import de.hpi.cache.services.CacheService;
import de.hpi.cache.services.IdealoBridge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;
    private final IdealoBridge idealoBridge;

    @RequestMapping(value = "/getOffer/{shopID}", method = GET)
    public String getOffer(@PathVariable long shopID){
        getCacheService();
        return "";
    }

    @RequestMapping(value = "/warmup/{shopID}", method = GET)
    public void warmup(@PathVariable long shopID){
        getIdealoBridge().getOffers(shopID);

    }
}
