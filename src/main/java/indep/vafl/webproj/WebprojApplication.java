package indep.vafl.webproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableJpaRepositories("indep.vafl.datarepo")
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableAutoConfiguration
@EntityScan("indep.vafl.entity")
@EnableJpaAuditing
@EnableScheduling
@ComponentScan({"indep.vafl.config","indep.vafl.control","indep.vafl.service","indep.vafl.utility","indep.vafl.pages"})
public class WebprojApplication {
	public static void main(String[] args) { 
		SpringApplication.run(WebprojApplication.class, args);
	}

}

