package evolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author Roman Hayda, 03.10.2017
 */

@Configuration
@EnableResourceServer
@EnableOAuth2Client //facebook
//@EnableConfigurationProperties
@Order(99)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//    @Value("${security.oauth2.resource.id}")
//    private String authServerResourceId;

    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private TokenStore tokenStore;
//    @Autowired
//    @Qualifier("dataSource")
//    private DataSource dataSource;

    /**
     * Method configures {@link HttpSecurity} to secure resources
     *
     * For explanation between configuration {@link HttpSecurity} in {@link WebSecurityConfigurerAdapter}
     * and {@link ResourceServerConfigurerAdapter} follow:
     * https://stackoverflow.com/questions/44234159/using-websecurityconfigureradapter-with-spring-oauth2-and-user-info-uri
     * https://stackoverflow.com/questions/38034217/spring-boot-and-oauth2-websecurityconfigureradapter-vs-resourceserverconfigurer
     * https://stackoverflow.com/questions/37288715/spring-oauth2-httpsecurity-http-resourceserverconfigurer-and-websecurityconf?rq=1
     * https://stackoverflow.com/questions/45980267/resourceserverconfigureradapter-vs-websecurityconfigureradapter
     * https://github.com/spring-projects/spring-security-oauth/issues/1024
     *
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .anonymous()
//                .disable()
//                .and()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/register/**", "/callback/**",
                        "/interlink/**",
                        "/callback2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS);
//                .and()
//                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config
//                .resourceId(authServerResourceId)
                .tokenServices(tokenServices)
                .tokenStore(tokenStore);
    }






    //facebook

//    @Autowired
//    @Qualifier("oauth2ClientContext")
//    OAuth2ClientContext oauth2ClientContext;
//
//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setEnabled(true);
//        registration.setOrder(-100);
//        return registration;
//    }


//    @Bean
//    @ConfigurationProperties("github")
//    public ClientResources github() {
//        ClientResources clientResources = new ClientResources();
////        clientResources.getClient().setPreEstablishedRedirectUri("http://localhost:4000/uaa/login/github");
////        clientResources.getClient().setUseCurrentUri(false);
//        return clientResources;
//    }
//
//    /**
//     * Create {@link ClientResources} using {@link ConfigurationProperties} prefix
//     * and extracting data from properties file
//     */
//    @Bean
//    @ConfigurationProperties("facebook")
//    public ClientResources facebook() {
//        ClientResources clientResources = new ClientResources();
////        clientResources.getClient().setPreEstablishedRedirectUri("http://localhost:4000/uaa/login/facebook");
////        clientResources.getClient().setUseCurrentUri(false);
//        return clientResources;
//    }
//
//    /**
//     * Create {@link ClientResources} using {@link ConfigurationProperties} prefix
//     * and extracting data from properties file
//     */
//    @Bean
//    @ConfigurationProperties("google")
//    public ClientResources google() {
//        ClientResources clientResources = new ClientResources();
////        clientResources.getClient().setPreEstablishedRedirectUri("http://localhost:4000/uaa/login/google");
////        clientResources.getClient().setUseCurrentUri(false);
//        return clientResources;
//    }
//
//    /**
//     * Create composite filter to intercept login through Facebook, Google
//     */
//    private Filter ssoFilter() {
//        CompositeFilter filter = new CompositeFilter();
//        List<Filter> filters = new ArrayList<Filter>();
//        filters.add(ssoFilter(facebook(), "/login/facebook"));
//        filters.add(ssoFilter(google(), "/login/google"));
//        filters.add(ssoFilter(github(), "/login/github"));
//        filter.setFilters(filters);
//        return filter;
//    }
//
//    /**
//     * Create filter according to current {@link ClientResources} and intercepted URL path
//     */
//    private Filter ssoFilter(ClientResources client, String path) {
//        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
//        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
//        filter.setRestTemplate(template);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
//                client.getResource().getUserInfoUri(), client.getClient().getClientId());
//        tokenServices.setRestTemplate(template);
//        filter.setTokenServices(tokenServices);
//        return filter;
//    }

}
