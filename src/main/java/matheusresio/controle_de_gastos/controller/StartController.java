package matheusresio.controle_de_gastos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StartController {

	@GetMapping
	public String redirectToLogin() {
		return "redirect:/auth/login";
	}
}
