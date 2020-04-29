package com.psikku.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Bean
    TokenStore tokenStore(){
        JwtTokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
        return tokenStore;
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("12345");
        return tokenConverter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.httpBasic();
        http.authorizeRequests().antMatchers("/users/hello").hasRole("ADMIN")
                .antMatchers("/api/users/info").fullyAuthenticated()
                .antMatchers("/api/tests*/*").fullyAuthenticated()
                .antMatchers("/api/users/login").permitAll()
//                .antMatchers("/api/users/login").permitAll()
//            .antMatchers("/api/**").permitAll()
//                .antMatchers("/api/**").hasRole("USER")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // setup cors configuration for accepting any request origins, certain request methods, and certain request headers
        http.cors(corsConfigurer ->{
            CorsConfigurationSource corsConfigurationSource = request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
                corsConfiguration.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE"));
                corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));

                return corsConfiguration;
            };
            corsConfigurer.configurationSource(corsConfigurationSource);
        });
    }
}
