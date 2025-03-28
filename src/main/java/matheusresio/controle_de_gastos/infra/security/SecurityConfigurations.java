package matheusresio.controle_de_gastos.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfigurations {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/css/**", "/images/**", "/js/**", "/").permitAll()
					.requestMatchers(HttpMethod.GET, "/home").authenticated()
					.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/auth/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/users/register").permitAll()
					.requestMatchers(HttpMethod.POST, "/users/register").permitAll()
					.anyRequest().authenticated())
		    .formLogin(form -> form
		    		.usernameParameter("email")
		    		.passwordParameter("password")
		    		.loginPage("/auth/login")
		    		.loginProcessingUrl("/auth/login")
		    		.defaultSuccessUrl("/home", true)
		    		.failureUrl("/auth/login?error=true")
		    		.permitAll())
		    .logout(logout -> logout
		              .logoutUrl("/logout")
		              .logoutSuccessUrl("/auth/login?logout=true")
		              .invalidateHttpSession(true)
		              .deleteCookies("JSESSIONID")
		              .permitAll()
		         );
			
		
		return http.build();
	}
	
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}


















