package com.psikku.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
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
        http.authorizeRequests()
                .mvcMatchers("/api/users/info").fullyAuthenticated()
                .mvcMatchers("/api/tests*/*").fullyAuthenticated()
                .mvcMatchers("/api/users/login").permitAll()
                .mvcMatchers("/api/users/register").permitAll()
                .mvcMatchers("/api/users/reset-password*/*").permitAll()
                .mvcMatchers("/api/packages/*").fullyAuthenticated()
                .mvcMatchers("/api/packages/internal*/*").permitAll()
                .mvcMatchers("/api/content*/*").fullyAuthenticated()
                .mvcMatchers("/api/mail-sender*/*").fullyAuthenticated()
                .mvcMatchers("/api/result*/*").permitAll()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // setup cors configuration for accepting any request origins, certain request methods, and certain request headers
        http.cors(corsConfigurer ->{
            CorsConfigurationSource corsConfigurationSource = request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
                corsConfiguration.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE"));
                corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Voucher","Content-Disposition"));

                return corsConfiguration;
            };
            corsConfigurer.configurationSource(corsConfigurationSource);
        });

    }
}
