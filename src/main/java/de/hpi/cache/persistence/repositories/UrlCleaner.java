package de.hpi.cache.persistence.repositories;

import de.hpi.cache.dto.SuccessCleanResponse;
import de.hpi.cache.properties.UrlCleanerProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Repository
@RequiredArgsConstructor
@Getter
public class UrlCleaner {

    private final UrlCleanerProperties properties;
    private final RestTemplate restTemplate;


    @Retryable(
            value = { HttpClientErrorException.class },
            backoff = @Backoff(delay = 3000, multiplier = 5))
    public String cleanUrl(String dirtyUrl, long shopID, String rootUrl) {
        return getRestTemplate().getForObject(getCleanURI(shopID, dirtyUrl, rootUrl), SuccessCleanResponse.class).getData()
                .getUrl();
    }


    private URI getCleanURI(long shopID, String dirtyUrl, String rootUrl) {
        return UriComponentsBuilder.fromUriString(getProperties().getUrl())
                .path(getProperties().getShopIdToUrlRoute() + shopID)
                .queryParam("url", dirtyUrl)
                .queryParam("shopRootUrl", rootUrl)
                .build()
                .encode()
                .toUri();
    }
}
