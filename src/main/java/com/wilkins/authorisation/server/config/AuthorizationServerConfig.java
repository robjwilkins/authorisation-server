package com.wilkins.authorisation.server.config;

import com.wilkins.authorisation.server.properties.ServiceProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;

@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ServiceProperties serviceProperties;
	private final TokenStore tokenStore;
	private final JwtAccessTokenConverter accessTokenConverter;
	private final AuthenticationManager authenticationManager;
	private final HandlerInterceptor tokenHandlerInterceptor;
    private final WebResponseExceptionTranslator exceptionTranslator;
    private final UserDetailsService userDetailsService;

    @Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer
		        .inMemory()
		        .withClient(serviceProperties.getClientId())
		        .secret(serviceProperties.getClientSecret())
		        .authorizedGrantTypes(serviceProperties.getGrantType(), "refresh_token")
		        .scopes(serviceProperties.getScopeRead(), serviceProperties.getScopeWrite())
                .accessTokenValiditySeconds(serviceProperties.getAccessTokenValidSeconds())
                .refreshTokenValiditySeconds(serviceProperties.getRefreshTokenValidSeconds())
		        .resourceIds(serviceProperties.getResourceId());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Collections.singletonList(accessTokenConverter));
		endpoints.tokenStore(tokenStore)
		        .accessTokenConverter(accessTokenConverter)
		        .tokenEnhancer(enhancerChain)
				.addInterceptor(tokenHandlerInterceptor)
                .exceptionTranslator(exceptionTranslator)
		        .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
	}

}
