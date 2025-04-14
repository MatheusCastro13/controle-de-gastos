package matheusresio.controle_de_gastos.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.CashFlow;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.CashFlowService;
import matheusresio.controle_de_gastos.service.DateRangeFilter;
import matheusresio.controle_de_gastos.service.MonthYearFilter;

@Controller
@RequestMapping("/home")
public class HomeController {

	private final AuthenticationService authenticationService;
	private final CashFlowService cashFlowService;

	@Autowired
	public HomeController(AuthenticationService authenticationService, CashFlowService cashFlowService) {
		this.authenticationService = authenticationService;
		this.cashFlowService = cashFlowService;
	}

	@GetMapping
	public String homePage(Model model,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "30") int size,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	    @RequestParam(required = false) Integer month,
	    @RequestParam(required = false) Integer year) {
	    
	    DateRangeFilter dateRangeFilter = (startDate != null && endDate != null)
	            ? new DateRangeFilter(startDate, endDate)
	            : null;
	    
	    MonthYearFilter monthYearFilter = (month != null && year != null)
	            ? new MonthYearFilter(month, year)
	            : null;

	    User user = authenticationService.getUserAuthenticated();
	    CashFlow cashFlow = cashFlowService.userCashFlow(user, page, size, dateRangeFilter, monthYearFilter);
	    model.addAttribute("user", user);
	    model.addAttribute("cashFlow", cashFlow);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", cashFlow.getTransactions().getTotalPages());
	    return "home";
	}

}
