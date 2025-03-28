package matheusresio.controle_de_gastos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/register")
	public String registerUserPage() {
		return "user-register";
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody UserRegister userRegister){
		userService.save(userRegister);
		return ResponseEntity.ok().build();
	}
}
