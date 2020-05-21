package space.netbytes.adshunters.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

public class KeycloakSecurityConfiguration {

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
    @KeycloakConfiguration
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
    public static class KeycloakConfigurationAdapter extends KeycloakWebSecurityConfigurerAdapter {

        /**
         * Defines the session authentication strategy.
         */
        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new NullAuthenticatedSessionStrategy();
        }

        /**
         * Registers the KeycloakAuthenticationProvider with the authentication manager.
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            // simple Authority Mapper to avoid ROLE_
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        /**
         * Required to handle spring boot configurations
         * @return
         */
        @Bean
        public KeycloakConfigResolver KeycloakConfigResolver() {
            return new KeycloakSpringBootConfigResolver();
        }

        @Bean
        public CorsFilter corsFilter() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

            final CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedOrigins(Collections.singletonList("*"));
            config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }

        /**
         * Configuration spécifique à keycloak (ajouts de filtres, etc)
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http
                    // disable csrf because of API mode
                    .csrf().disable()
                    .sessionManagement()
                    // use previously declared bean
                    .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    // keycloak filters for securisation
                    .and()
                    .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                    .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                    // delegate logout endpoint to spring security
                    .and()
                    .logout()
                    .addLogoutHandler(keycloakLogoutHandler())
                    // logout handler for API
                    .logoutUrl("/logout").logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                    .and()
                    // manage routes securisation here
                    .authorizeRequests().anyRequest().permitAll();
            //.authorizeRequests().antMatchers(HttpMethod.GET, "/domains").permitAll()
            //.antMatchers(HttpMethod.GET, "/categories").hasRole("ADMIN")
            //.anyRequest().denyAll();
        }
    }
}
