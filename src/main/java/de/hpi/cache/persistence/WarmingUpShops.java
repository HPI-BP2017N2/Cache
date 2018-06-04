package de.hpi.cache.persistence;


import lombok.AccessLevel;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
public class WarmingUpShops {

    private final List<Long> currentlyWarmingUp = new LinkedList<>();

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
