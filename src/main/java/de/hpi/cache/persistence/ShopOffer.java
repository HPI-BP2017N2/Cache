package de.hpi.cache.persistence;

import de.hpi.cache.dto.IdealoOffer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopOffer extends IdealoOffer {

    @Indexed
    private short phase;

}
