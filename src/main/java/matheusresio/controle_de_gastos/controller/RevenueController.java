package matheusresio.controle_de_gastos.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.RevenueDto;
import matheusresio.controle_de_gastos.model.dto.RevenueResponse;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.DateRangeFilter;
import matheusresio.controle_de_gastos.service.MonthYearFilter;
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
	public String revenuesPage(Model model,
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
		Page<Revenue> revenues = revenueService.findAllByUser(user, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))), dateRangeFilter, monthYearFilter);

		model.addAttribute("revenues", revenues.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", revenues.getTotalPages());
		model.addAttribute("user", user);
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
