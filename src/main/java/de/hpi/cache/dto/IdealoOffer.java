package de.hpi.cache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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


}
