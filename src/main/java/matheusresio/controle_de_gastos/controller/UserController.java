package matheusresio.controle_de_gastos.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

		userService.save(userRegister);
		return ResponseEntity.ok().build();
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

		userService.changePassword(id, user, password);
		return ResponseEntity.ok().build();

	}

	@PostMapping("/update/{id}")
	public ResponseEntity<Void> updateUserInfos(@PathVariable UUID id, @RequestBody UserUpdateDto userDto) {
		User user = authenticationService.getUserAuthenticated();
		userService.update(id, user, userDto);
		return ResponseEntity.ok().build();
	}
}
