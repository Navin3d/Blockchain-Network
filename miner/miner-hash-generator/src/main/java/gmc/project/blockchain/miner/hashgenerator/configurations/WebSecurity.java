package gmc.project.blockchain.miner.hashgenerator.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurity {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {		
		http
			.authorizeRequests().anyRequest().permitAll()
		.and()
			.headers().frameOptions().disable()
		.and()
			.csrf().disable();
		return http.build();
	}

}
