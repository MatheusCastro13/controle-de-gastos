package matheusresio.controle_de_gastos.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	private final AuthenticationService authenticationService;
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService, AuthenticationService authenticationService) {
		this.userService = userService;
		this.authenticationService = authenticationService;
	}
	
	@GetMapping("/register")
	public String registerUserPage() {
		return "user-register";
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody UserRegister userRegister){
		try {
			userService.save(userRegister);
			return ResponseEntity.ok().build();
		}
		catch(CredentialsAlreadyUsedException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		catch(EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public String getUserById(@PathVariable UUID id, Model model) {
		User user = authenticationService.getUserAuthenticated();
		
		try {
			userService.userProfileCredentials(user, id);
			model.addAttribute("user", user);
			return "user-profile";
		}
		catch(AccessDeniedException e) {
			e.printStackTrace();
			return "" + HttpStatus.UNAUTHORIZED;
		}
	}
}














