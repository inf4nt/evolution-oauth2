package evolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .anonymous()
//                .disable()
                .httpBasic().disable()
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/register/**", "/callback/**",
                        "/interlink/**",
                        "/callback2/**").permitAll()
                .anyRequest().authenticated();

//        if (securityProperties.isRequireSsl()){
//            http.requiresChannel().anyRequest().requiresSecure();
//        }

    }

//@PreAuthorize("${oauth2.hasScope('ui')}")
    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(Encoder.BCrypt8.instance);

//        auth.authenticationProvider(new DaoAuthenticationProvider());

        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
        .authorities("ROLE_ADMIN")
        .and()
        .withUser("user")
        .password("user")
        .authorities("ROLE_USER");
    }









    //    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//        web.ignoring()
//                .antMatchers("/uaa/registration/*",
//                        "/uaa/register/**", "/uaa/register/google",
//                        "/uaa/callback/**", "/uaa/callback/google");
//
//    }

}