package de.hpi.cache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdealoOffer {
    @Indexed
    private String offerKey;
    private long shopId;
    private String brandName;
    private List<String> categoryPaths;
    private Map<String, String> productSearchtext;
    private String ean;
    private String han;
    private String sku;
    private Map<String, String> titles;
    private Map<String, Double> prices;
    private Map<String, String> descriptions;
    private Map<String, String> urls;
    private List<String> hans;
    private List<String>  eans;

    @JsonCreator
    public IdealoOffer(
            @JsonProperty("offerKey") String offerKey,
            @JsonProperty("shopId") long shopId,
            @JsonProperty("brandName") String brandName,
            @JsonProperty("categoryPaths") List<String> categoryPaths,
            @JsonProperty("productSearchtext") Map<String, String> productSearchtext,
            @JsonProperty("ean") String ean,
            @JsonProperty("han") String han,
            @JsonProperty("sku") String sku,
            @JsonProperty("titles") Map<String, String> titles,
            @JsonProperty("prices") Map<String, Double> prices,
            @JsonProperty("descriptions") Map<String, String> descriptions,
            @JsonProperty("urls") Map<String, String> urls,
            @JsonProperty("hans") List<String> hans,
            @JsonProperty("eans") List<String>  eans

    ) {
        setOfferKey(offerKey);
        setShopId(shopId);
        setBrandName(brandName);
        setCategoryPaths(categoryPaths);
        setProductSearchtext(productSearchtext);
        setEan(ean);
        setHan(han);
        setSku(sku);
        setTitles(titles);
        setPrices(prices);
        setDescriptions(descriptions);
        setUrls(urls);
        setHans(hans);
        setEans(eans);
    }
}
