package de.hpi.cache.api;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import de.hpi.cache.services.CacheService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;
    private final ShopOfferRepositoryImpl shopOfferRepository;

    @RequestMapping(value = "/getOffer/{shopID}", method = RequestMethod.GET, produces = "application/json")
    public ShopOffer getOffer(@PathVariable long shopID, @RequestParam(value = "phase") byte phase){
        return getCacheService().getOffer(shopID, phase);
    }

    @RequestMapping(value = "/deleteOffer/{shopID}", method = RequestMethod.DELETE)
    public void deleteOffer(@PathVariable long shopID, @RequestParam(value = "offerKey") String offerKey){
        getCacheService().deleteOffer(shopID, offerKey);
    }

    @RequestMapping(value = "/deleteAll/{shopID}", method = RequestMethod.DELETE)
    public void deleteAll(@PathVariable long shopID){
        getCacheService().deleteAll(shopID);
    }

    @RequestMapping(value = "/warmup/{shopID}", method = RequestMethod.GET)
    public void warmup(@PathVariable long shopID){
        getCacheService().warmup(shopID);
    }
}
