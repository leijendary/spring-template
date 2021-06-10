package com.leijendary.spring.microservicetemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.leijendary.spring.microservicetemplate.data.request.OAuthRequest;
import com.leijendary.spring.microservicetemplate.data.response.OAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import static java.util.Optional.ofNullable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public abstract class ApplicationTests {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    public String contextPath;

    @Value("${oauth.url}")
    public String url;

    @Value("${oauth.client-key}")
    public String clientKey;

    @Value("${oauth.client-secret}")
    public String clientSecret;

    @Value("${oauth.username}")
    public String username;

    @Value("${oauth.password}")
    public String password;

    @Test
    void contextLoads() {
    }

    public String token(final String ...scopes) {
        final var basicAuth = Base64.getEncoder()
                .encodeToString((clientKey + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        final var headers = new HttpHeaders();
        headers.put("Authorization", Collections.singletonList("Basic " + basicAuth));

        final var scope = ofNullable(scopes)
                .map(strings -> String.join(" ", strings))
                .orElse("");
        final var body = new OAuthRequest();
        body.setUsername(username);
        body.setPassword(password);
        body.setScope(scope);

        final var jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(jacksonObjectMapper());

        final var messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(jsonMessageConverter);

        final var entity = new HttpEntity<>(body, headers);
        final var result = new RestTemplate(messageConverters)
                .postForEntity(url, entity, OAuthResponse.class)
                .getBody();

        Assertions.assertNotNull(result);

        log.info("Got Access Token: {}", result.getAccessToken());

        return result.getAccessToken();
    }

    protected RestTemplateBuilder restTemplateWithToken(final String ...scopes) {
        return new RestTemplateBuilder()
                .defaultHeader("Authorization", "Bearer " + token(scopes))
                .rootUri("http://localhost:" + port + contextPath);
    }

    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper().setPropertyNamingStrategy(propertyNamingStrategy());
    }

    public PropertyNamingStrategy propertyNamingStrategy() {
        return new PropertyNamingStrategy.SnakeCaseStrategy();
    }
}
