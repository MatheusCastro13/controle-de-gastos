package matheusresio.controle_de_gastos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

}
