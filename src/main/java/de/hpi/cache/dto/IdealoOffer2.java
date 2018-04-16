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
public class IdealoOffer2 {
    @Indexed
    private Map<String, String> offerKey;
    private Map<String, Long> shopId;
    private Map<String, String> brandName;
    private Map<String, List<String>> categoryPaths;
    private Map<String, String> productSearchtext;
    private Map<String, String> ean;
    private Map<String, String> han;
    private Map<String, String> sku;
    private Map<String, Map<String, String>> titles;
    private Map<String, Map<String, Double>> prices;
    private Map<String, Map<String, String>> descriptions;
    private Map<String, Map<String, String>> urls;
    private Map<String, List<String>> hans;
    private Map<String, List<String>> eans;

}
