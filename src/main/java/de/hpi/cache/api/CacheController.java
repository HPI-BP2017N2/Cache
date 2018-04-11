package de.hpi.cache.api;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.persistence.ShopOffer;
import de.hpi.cache.persistence.repositories.ShopOfferRepositoryImpl;
import de.hpi.cache.services.CacheService;
import de.hpi.cache.services.IdealoBridge;
import de.hpi.cache.services.WarmupService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;
    private final WarmupService warmupService;
    private final ShopOfferRepositoryImpl shopOfferRepository;

    @RequestMapping(value = "/getOffer/{shopID}", method = RequestMethod.GET, produces = "application/json")
    public IdealoOffer getOffer(@PathVariable long shopID, @RequestParam(value = "phase") short phase){
        return getShopOfferRepository().getOffer(shopID, phase);
    }

    @RequestMapping(value = "deleteOffer/{shopID}", method = RequestMethod.GET)
    public void deleteOffer(@PathVariable long shopID, @RequestParam(value = "offerKey") String offerKey){
        getShopOfferRepository().deleteOffer(shopID, offerKey);
    }

    @RequestMapping(value = "deleteAll/{shopID}", method = RequestMethod.GET)
    public void deleteAll(@PathVariable long shopID){
        getShopOfferRepository().deleteAll(shopID);
    }

    @RequestMapping(value = "/warmup/{shopID}", method = GET)
    public void warmup(@PathVariable long shopID){
        getWarmupService().warmup(shopID);
    }
}
