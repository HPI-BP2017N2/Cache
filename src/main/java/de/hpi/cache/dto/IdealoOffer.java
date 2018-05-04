package de.hpi.cache.dto;

import de.hpi.cache.persistence.ShopOffer;
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

    private Property<String> offerKey;
    private Property<Long> shopId;
    private Property<String> brandName;
    private Property<List<String>> categoryPaths;
    private Property<String> productSearchtext;
    private Property<String> ean;
    private Property<String> han;
    private Property<String> sku;
    private Property<Map<String, String>> titles;
    private Property<Map<String, Double>> prices;
    private Property<Map<String, String>> descriptions;
    private Property<Map<String, String>> urls;
    private Property<List<String>> hans;
    private Property<List<String>> eans;
    private Property<Map<String, String>> smallPicture;
    private Property<Map<String, List<String>>> imageUrls;
    private Property<String> productKey;
    private Property<Long> mappedCatalogCategory;


    public ShopOffer toShopOffer() {
        ShopOffer shopOffer = new ShopOffer();
        shopOffer.setOfferKey(getPropertyValue(getOfferKey()));
        shopOffer.setShopId(getPropertyValue(getShopId()));
        shopOffer.setBrandName(getPropertyValue(getBrandName()));
        shopOffer.setCategoryPaths(getPropertyValue(getCategoryPaths()));
        shopOffer.setProductSearchtext(getPropertyValue(getProductSearchtext()));
        shopOffer.setEan(getPropertyValue(getEan()));
        shopOffer.setHan(getPropertyValue(getHan()));
        shopOffer.setSku(getPropertyValue(getSku()));
        shopOffer.setTitles(getPropertyValue(getTitles()));
        shopOffer.setPrices(getPropertyValue(getPrices()));
        shopOffer.setDescriptions(getPropertyValue(getDescriptions()));
        shopOffer.setUrls(getPropertyValue(getUrls()));
        shopOffer.setHans(getPropertyValue(getHans()));
        shopOffer.setEans(getPropertyValue(getEans()));
        shopOffer.setSmallPicture(getPropertyValue(getSmallPicture()));
        shopOffer.setImageUrls(getPropertyValue(getImageUrls()));
        shopOffer.setProductKey(getPropertyValue(getProductKey()));
        shopOffer.setMappedCatalogCategory(getPropertyValue(getMappedCatalogCategory()));
        return shopOffer;
    }

    private <T> T getPropertyValue(Property<T> property){
        return (property == null)? null : property.getValue();
    }
}


