package matheusresio.controle_de_gastos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.UserRegisterAdm;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.UserService;

@Controller
@RequestMapping("/adm")
public class AdmController {

	private final AuthenticationService authenticationService;
	private final UserService userService;
	
	@Autowired
	public AdmController(AuthenticationService authenticationService, UserService userService) {
		this.authenticationService = authenticationService;
		this.userService = userService;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String admPage(Model model) {
		User user = authenticationService.getUserAuthenticated();
		model.addAttribute("user", user);
		return "admin";
	}
	
	@PostMapping("/save/user")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> saveNewUser(@RequestBody UserRegisterAdm userDto) {
		try {
			userService.save(userDto);
			return ResponseEntity.ok().build();
		}
		catch(CredentialsAlreadyUsedException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		catch(EntityNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	
}
