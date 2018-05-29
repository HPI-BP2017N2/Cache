package de.hpi.cache.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ShopOffer {

    @Id private String offerKey;
    @Indexed private byte phase = 0;
    @Indexed private boolean isMatched = false;
    private Long shopId;
    private String brandName;
    private List<String> categoryPaths;
    private String productSearchtext;
    private String ean;
    private String han;
    private String sku;
    private Map<String, String> titles;
    private Map<String, Double> prices;
    private Map<String, String> descriptions;
    private Map<String, String> urls;
    private List<String> hans;
    private List<String> eans;
    private Map<String, String> smallPicture;
    private Map<String, List<String>> imageUrls;
    private String productKey;
    private String mappedCatalogCategory;
    private String categoryName;
    private String higherLevelCategory;
    private String higherLevelCategoryName;
    private String imageId;

}
