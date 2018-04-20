package de.hpi.cache.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.CloseableIterator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class OfferList implements CloseableIterator<IdealoOffer> {

    private List<IdealoOffer> offers;

    public OfferList(List<IdealoOffer> offers) {
        setOffers(offers);
    }

    @Override
    public IdealoOffer next() {
        IdealoOffer offer = getOffers().get(0);
        getOffers().remove(0);
        return offer;
    }

    @Override
    public boolean hasNext() {
        return (offers.size() > 0);
    }

    @Override
    public void remove() {
        throw new NotImplementedException();
    }

    @Override
    public void close() {

    }
}