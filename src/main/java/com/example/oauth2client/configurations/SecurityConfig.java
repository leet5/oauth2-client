package com.example.oauth2client.configurations;

import com.example.oauth2client.services.IngredientService;
import com.example.oauth2client.services.RestIngredientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(login -> login.loginPage("/oauth2/authorization/taco-admin-client"))
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @RequestScope
    public IngredientService ingredientService(OAuth2AuthorizedClientService clientService) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = null;

        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            final String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            if (clientRegistrationId.equals("taco-admin-client")) {
                final OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
                accessToken = client.getAccessToken().getTokenValue();
            }
        }
        return new RestIngredientService(accessToken);
    }
}
