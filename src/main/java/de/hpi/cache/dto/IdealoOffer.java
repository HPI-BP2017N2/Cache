package de.hpi.cache.dto;

import com.mongodb.util.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdealoOffer {
    private String offerKey;
    private long shopId;
    private String brandName;
    private List<String> categoryPaths;
    private List<String> productSearchtext;
    private String ean;
    private String han;
    private String sku;
    private List<String> titles;
    private List<Double> prices;
    private List<String> descriptions;
    private List<String> urls;
    private List<String> hans;
    private List<String>  eans;
}
