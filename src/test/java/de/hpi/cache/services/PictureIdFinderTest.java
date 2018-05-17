package de.hpi.cache.services;

import de.hpi.cache.dto.IdealoOffer;
import de.hpi.cache.dto.Property;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

@Getter(AccessLevel.PRIVATE)
public class PictureIdFinderTest {

    @Getter(AccessLevel.PRIVATE) private static String EXAMPLE_URL = "http://example.com/123";
    @Getter(AccessLevel.PRIVATE) private static String EXAMPLE_IMAGE_URL1 = "http://example.com/124";
    @Getter(AccessLevel.PRIVATE) private static String EXAMPLE_IMAGE_URL2 = "http://example.com/125";
    @Getter(AccessLevel.PRIVATE) private static String[] EXPECTED_URL_PARTS = {"http:", "example.com", "123"};
    @Getter(AccessLevel.PRIVATE) private static int[] EXPECTED_IMAGE_IDS = {2};

    private final List<IdealoOffer> offers = new LinkedList<>();

    @Before
    public void setup() {

        Property<Map<String, List<String>>> imageUrlProperty1 = new Property<>();
        Property<Map<String, List<String>>> imageUrlProperty2 = new Property<>();
        Map<String, List<String>> imageUrls1 = new HashMap<>();
        Map<String, List<String>> imageUrls2 = new HashMap<>();
        List<String> imageUrl1 = new LinkedList<>();
        List<String> imageUrl2 = new LinkedList<>();

        imageUrl1.add(getEXAMPLE_IMAGE_URL1());
        imageUrl2.add(getEXAMPLE_IMAGE_URL2());
        imageUrls1.put("0", imageUrl1);
        imageUrls2.put("0", imageUrl2);

        imageUrlProperty1.setValue(imageUrls1);
        imageUrlProperty2.setValue(imageUrls2);

        IdealoOffer offer1 = new IdealoOffer();
        IdealoOffer offer2 = new IdealoOffer();
        offer1.setImageUrls(imageUrlProperty1);
        offer2.setImageUrls(imageUrlProperty2);

        getOffers().add(offer1);
        getOffers().add(offer2);

    }

    @Test
    public void findPictureId() {
        List<Integer> imageIds = PictureIdFinder.findPictureId(getOffers());

        assertEquals(getEXPECTED_IMAGE_IDS().length, imageIds.size());

        for(int i = 0; i < getEXPECTED_IMAGE_IDS().length; i++) {
            assertEquals(getEXPECTED_IMAGE_IDS()[i], (int)imageIds.get(i));
        }
    }

    @Test
    public void splitUrl() {
        String[] result = PictureIdFinder.splitUrl(getEXAMPLE_URL());

        assertEquals(getEXPECTED_URL_PARTS().length, result.length);

        for(int i = 0; i < getEXPECTED_URL_PARTS().length; i++) {
            assertEquals(getEXPECTED_URL_PARTS()[i], result[i]);
        }
    }
}