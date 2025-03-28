package matheusresio.controle_de_gastos.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.ExpenseDto;
import matheusresio.controle_de_gastos.model.dto.ExpenseResponse;
import matheusresio.controle_de_gastos.service.AuthenticationService;
import matheusresio.controle_de_gastos.service.ExpenseService;

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
	public String expensesPage(Model model) {
		User user = authenticationService.getUserAuthenticated();
		List<Expense> expenses = user.getExpenses();
		expenses.sort(Comparator.comparing(Expense::getId, Comparator.reverseOrder()));
		
		model.addAttribute("user", user);
		model.addAttribute("expenses", expenses);
		return "expenses";
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> saveNewReveue(@RequestBody ExpenseDto expenseDto) {
		User user = authenticationService.getUserAuthenticated();
		try {
			expenseService.save(expenseDto, user);
			return ResponseEntity.ok().build();
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateReveue(@RequestBody ExpenseDto expenseDto, @PathVariable Long id) {
		User user = authenticationService.getUserAuthenticated();
		
		try {
			expenseService.update(id, expenseDto, user);
			return ResponseEntity.ok().build();
		}
		catch(EntityNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteReveue(@PathVariable Long id) {
		User user = authenticationService.getUserAuthenticated();
		
		try {
			expenseService.delete(id, user);
			return ResponseEntity.ok().build();
		}
		catch(EntityNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> findById(@PathVariable Long id){
		
		try {
			ExpenseResponse expense = expenseService.findResponseById(id);
			return ResponseEntity.ok(expense);
		}
		catch(EntityNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
}





















