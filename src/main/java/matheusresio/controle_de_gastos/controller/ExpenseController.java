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

import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.ExpenseDto;
import matheusresio.controle_de_gastos.model.dto.ExpenseResponse;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.DateRangeFilter;
import matheusresio.controle_de_gastos.service.ExpenseService;
import matheusresio.controle_de_gastos.service.MonthYearFilter;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

	private final AuthenticationService authenticationService;
	private final ExpenseService expenseService;

	@Autowired
	public ExpenseController(AuthenticationService authenticationService, ExpenseService expenseService) {
		this.authenticationService = authenticationService;
		this.expenseService = expenseService;
	}

	@GetMapping
	public String expensesPage(Model model,
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
		Page<Expense> expenses = expenseService.findByUser(user, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))), dateRangeFilter, monthYearFilter);
		model.addAttribute("expenses", expenses.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", expenses.getTotalPages());
		model.addAttribute("user", user);
		return "expenses";
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveNewExpense(@RequestBody ExpenseDto expenseDto) {
		User user = authenticationService.getUserAuthenticated();

		expenseService.save(expenseDto, user);
		return ResponseEntity.ok().build();

	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateReveue(@RequestBody ExpenseDto expenseDto, @PathVariable Long id) {
		User user = authenticationService.getUserAuthenticated();

		expenseService.update(id, expenseDto, user);
		return ResponseEntity.ok().build();

	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteReveue(@PathVariable Long id) {
		User user = authenticationService.getUserAuthenticated();

		expenseService.delete(id, user);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> findById(@PathVariable Long id) {

		ExpenseResponse expense = expenseService.findResponseById(id);
		return ResponseEntity.ok(expense);

	}

}
