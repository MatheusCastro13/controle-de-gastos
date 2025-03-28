package matheusresio.controle_de_gastos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.model.User;

@Service
public class AuthenticationService {

	@Autowired
	private UserService userService;
	
	public User getUserAuthenticated() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.isAuthenticated() &&
	            !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
	            
	            String email = authentication.getName();
	            User user = userService.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
	            if (user != null) {
	                return user;
	            }
	        }
		return null;
	}
}
