package de.hpi.cache.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("urlcleaner")
@Getter
@Setter
@Primary
public class UrlCleanerProperties {

    private String url;
    private String shopIdToUrlRoute;

}
