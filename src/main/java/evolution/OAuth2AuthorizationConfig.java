package evolution;

//import com.amberity.authservice.server.service.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * @author Roman Hayda, 02.10.2017
 */

@Configuration
@EnableAuthorizationServer
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${evolution.certificate.key}")
    private String secretKey;

    @Value("${evolution.accessTokenValiditySeconds}")
    private Integer accessTokenValiditySeconds;

    @Value("${evolution.refreshTokenValiditySeconds}")
    private Integer refreshTokenValiditySeconds;


    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private CustomTokenEnhancer tokenEnhancer;
//
//    @Autowired
//    private UserDetailsService userDetailsService;

//		@Autowired
//		private Environment env;

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//
//    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()

                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("ui")
//                .autoApprove(true)
//                .authorities("ACTUATOR")
                .accessTokenValiditySeconds(accessTokenValiditySeconds) //Access token is only valid for ? minutes.
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds) //Refresh token is only valid for ? minutes.
//				.resourceIds(authServerResourceId/*, "oauth2-resource"*/) //определяет к каким ресурсам может обращаться

                .and()
                .withClient("adminka")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("server", "ui")
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)

                .and()
                .withClient("auth")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                .scopes("server")
                .authorities("ROLE_ADMIN")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(600)

                .and()
                .withClient("customer-service")
//					.secret(env.getProperty("DEMO_SERVICE_PASSWORD"))
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                .scopes("server")
//                .resourceIds(authServerResourceId, "oauth2-resource")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)

                .and()
                .withClient("listing-service")
//					.secret(env.getProperty("DEMO_SERVICE_PASSWORD"))
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
//                .resourceIds(authServerResourceId, "oauth2-resource")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)

                .and()
                .withClient("catalog-service")
//					.secret(env.getProperty("DEMO_SERVICE_PASSWORD"))
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
//                .resourceIds(authServerResourceId, "oauth2-resource")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)


                //dev services ====================
                .and()
                .withClient("mail-service")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)

                .and()
                .withClient("ask-service")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "authorization_code")
                .scopes("server", "ui")
                .authorities("ROLE_ADMIN")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(600)

                .and()
                .withClient("demo-service")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "authorization_code")
                .scopes("server")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(600);
                //========================================


    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
//                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
//				.allowFormAuthenticationForClients() //??
//                .sslOnly()
//                .configure()
//                .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
//        return new JdbcTokenStore(dataSource());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("jwt.jks"), secretKey.toCharArray())
                .getKeyPair("jwt");
        converter.setKeyPair(keyPair);
        return converter;
    }
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(
//                Arrays.asList(tokenEnhancer, jwtAccessTokenConverter()));
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
//        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        return defaultTokenServices;
    }
}
