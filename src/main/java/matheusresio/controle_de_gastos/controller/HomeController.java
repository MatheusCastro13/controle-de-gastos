package matheusresio.controle_de_gastos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.CashFlow;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.CashFlowService;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	private final AuthenticationService authenticationService;
	private final CashFlowService cahsFlowService;
	
	@Autowired
	public HomeController(AuthenticationService authenticationService, CashFlowService cahsFlowService) {
		this.authenticationService = authenticationService;
		this.cahsFlowService = cahsFlowService;
	}

	@GetMapping
	public String homePage(Model model) {
		User user = authenticationService.getUserAuthenticated();
		CashFlow cashFlow = cahsFlowService.userCashFlow(user);
		model.addAttribute("user", user);
		model.addAttribute("cashFlow", cashFlow);
		
		return "home";
	}
}
