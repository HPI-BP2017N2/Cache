package de.hpi.cache.persistence;


import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter(AccessLevel.PRIVATE)
public class WarmingUpShops {

    private final Set<Long> currentlyWarmingUp = new HashSet<>();

    public boolean isWarmingUp(long shopId) {
        return getCurrentlyWarmingUp().contains(shopId);
    }

    public void addShop(long shopId) {
        getCurrentlyWarmingUp().add(shopId);
    }

    public void removeShop(long shopId) {
        getCurrentlyWarmingUp().remove(shopId);
    }
}
