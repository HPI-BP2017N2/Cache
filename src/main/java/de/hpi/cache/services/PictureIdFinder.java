package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.Property;

import java.util.*;

public class PictureIdFinder {

    public static List<Integer> findPictureId(List<IdealoOffer> idealoOffers) {
        Set<String> partSet = new HashSet<>();
        int length = 0;
        Set<Integer> nonUniqueIndice = new HashSet<>();
        for (IdealoOffer offer : idealoOffers) {
            Property<Map<String, List<String>>> imageUrls = offer.getImageUrls();
            if (imageUrls == null) {
                continue;
            }
            String[] urlParts = splitUrl(imageUrls.getValue().get(imageUrls.getValue().keySet().iterator().next()).get(0));
            length = urlParts.length;

            for (int index = 0; index < length; index++ ) {
                if(partSet.contains(urlParts[index])){
                    nonUniqueIndice.add(index);
                }
                partSet.add(urlParts[index]);
            }
        }

        List<Integer> uniqueIndex = new ArrayList<>();
        for(int index = 0; index < length; index++ ) {
            if (!nonUniqueIndice.contains(index)) {
                uniqueIndex.add(index);
            }
        }
        return uniqueIndex;
    }

    public static String[] splitUrl(String url) {
        url = url.replace("//", "/");
        return url.split("/");
    }
}
