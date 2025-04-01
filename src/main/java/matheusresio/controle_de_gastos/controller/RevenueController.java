package matheusresio.controle_de_gastos.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.RevenueDto;
import matheusresio.controle_de_gastos.model.dto.RevenueResponse;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.RevenueService;

@Controller
@RequestMapping("/revenues")
public class RevenueController {

	private final AuthenticationService authenticationService;
	private final RevenueService revenueService;

	@Autowired
	public RevenueController(AuthenticationService authenticationService, RevenueService revenueService) {
		this.authenticationService = authenticationService;
		this.revenueService = revenueService;
	}

	@GetMapping
	public String revenuesPage(Model model) {
		User user = authenticationService.getUserAuthenticated();
		List<Revenue> revenues = user.getRevenues();
		revenues.sort(Comparator.comparing(Revenue::getId, Comparator.reverseOrder()));

		model.addAttribute("user", user);
		model.addAttribute("revenues", revenues);
		return "revenues";
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveNewReveue(@RequestBody RevenueDto revenueDto) {
		User user = authenticationService.getUserAuthenticated();

		revenueService.save(revenueDto, user);
		return ResponseEntity.ok().build();

	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateReveue(@RequestBody RevenueDto revenueDto, @PathVariable Long id) {
		User user = authenticationService.getUserAuthenticated();

		revenueService.update(id, revenueDto, user);
		return ResponseEntity.ok().build();

	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteReveue(@PathVariable Long id) {
		User user = authenticationService.getUserAuthenticated();

		revenueService.delete(id, user);
		return ResponseEntity.ok().build();

	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> findById(@PathVariable Long id) {

		RevenueResponse revenue = revenueService.findResponseById(id);
		return ResponseEntity.ok(revenue);

	}

}
