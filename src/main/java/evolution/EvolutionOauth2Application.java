package evolution;

import evolution.client.feign.PeopleFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
@ComponentScan("evolution.server")
public class EvolutionOauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(EvolutionOauth2Application.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "security.oauth2.client")
	public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
		return new ClientCredentialsResourceDetails();
	}

	@Bean
	public OAuth2RestTemplate clientCredentialsRestTemplate() {
		return new OAuth2RestTemplate(clientCredentialsResourceDetails());
	}
}
