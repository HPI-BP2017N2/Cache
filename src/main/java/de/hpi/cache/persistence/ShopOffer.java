package de.hpi.cache.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ShopOffer {

    @Id
    private String offerKey;
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
    private byte phase = 0;
}
