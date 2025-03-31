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
import matheusresio.controle_de_gastos.exceptions.SameCredentialsException;
import matheusresio.controle_de_gastos.exceptions.SamePasswordException;
import matheusresio.controle_de_gastos.exceptions.WrongPasswordException;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.PasswordChange;
import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.model.dto.UserUpdateDto;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.AuthorizationService;
import matheusresio.controle_de_gastos.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	private final AuthenticationService authenticationService;
	private final UserService userService;
	private final AuthorizationService authorizationService;

	@Autowired
	public UserController(UserService userService, AuthenticationService authenticationService,
			AuthorizationService authorizationService) {
		this.userService = userService;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
	}

	@GetMapping("/register")
	public String registerUserPage() {
		return "user-register";
	}

	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody UserRegister userRegister) {
		try {
			userService.save(userRegister);
			return ResponseEntity.ok().build();
		} catch (CredentialsAlreadyUsedException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public String getUserById(@PathVariable UUID id, Model model) {
		User userAuthenticated = authenticationService.getUserAuthenticated();
		
		authorizationService.verifyUserAccess(userAuthenticated, id);
		
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "user-profile";

	}

	@PostMapping("/change-password/{id}")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChange password, @PathVariable UUID id, Model model) {
		User user = authenticationService.getUserAuthenticated();
		try {
			userService.changePassword(id, user, password);
			return ResponseEntity.ok().build();
		} catch (SamePasswordException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nova senha igual a anterior");
		}

		catch (WrongPasswordException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha atual não confere");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateUserInfos(@PathVariable UUID id, @RequestBody UserUpdateDto userDto) {
		User user = authenticationService.getUserAuthenticated();
		try {
			userService.update(id, user, userDto);
			return ResponseEntity.ok().build();
		} catch (CredentialsAlreadyUsedException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (AccessDeniedException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (SameCredentialsException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}
