package de.hpi.cache.api;

import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.WarmingUpShops;
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
    private final WarmingUpShops currentlyWarmingUp = new WarmingUpShops();

    @RequestMapping(value = "/getOfferAndUpdatePhase/{shopID}", method = RequestMethod.GET, produces = "application/json")
    public ShopOffer getOfferAndUpdatePhase(@PathVariable long shopID, @RequestParam(value = "phase") byte phase){
        return getCacheService().getOfferAndUpdatePhase(shopID, phase, getCurrentlyWarmingUp());
    }

    @RequestMapping(value = "/getUnmatchedOfferAndUpdatePhase/{shopID}", method = RequestMethod.GET, produces = "application/json")
    public ShopOffer getUnmatchedOfferAndUpdatePhase(@PathVariable long shopID, @RequestParam(value = "phase") byte phase){
        return getCacheService().getUnmatchedOfferAndUpdatePhase(shopID, phase);
    }


    @RequestMapping(value = "/getOffer/{shopID}", method = RequestMethod.GET, produces = "application/json")
    public ShopOffer getOffer(@PathVariable long shopID, @RequestParam(value = "offerKey") String offerKey){
        return getCacheService().getOffer(shopID, offerKey);
    }

    @RequestMapping(value = "/markAsMatched/{shopID}", method = RequestMethod.DELETE)
    public void markAsMatched(@PathVariable long shopID, @RequestParam(value = "offerKey") String offerKey){
        getCacheService().markAsMatched(shopID, offerKey);
    }

    @RequestMapping(value = "updatePhase/{shopID}", method = RequestMethod.POST)
    public void updatePhase(@PathVariable long shopID, @RequestParam(value = "oldPhase") byte oldPhase, @RequestParam(value = "newPhase") byte newPhase) {
        getCacheService().updatePhase(shopID, oldPhase, newPhase);
    }

    @RequestMapping(value = "/deleteAll/{shopID}", method = RequestMethod.DELETE)
    public void deleteAll(@PathVariable long shopID){
        getCacheService().deleteAll(shopID);
    }

    @RequestMapping(value = "/warmup/{shopID}", method = RequestMethod.GET)
    public void warmup(@PathVariable long shopID){
        getCacheService().warmup(shopID, getCurrentlyWarmingUp());
    }
}
