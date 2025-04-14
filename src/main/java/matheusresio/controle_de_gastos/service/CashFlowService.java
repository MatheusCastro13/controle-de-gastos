package matheusresio.controle_de_gastos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.CashFlow;
import matheusresio.controle_de_gastos.model.dto.Transaction;
import matheusresio.controle_de_gastos.model.enums.TransactionType;
import matheusresio.controle_de_gastos.repository.TransactionRepository;
import matheusresio.controle_de_gastos.repository.specifications.TransactionSpecifications;

@Service
public class CashFlowService {
	
	private final TransactionRepository transactionRepository;
	
	@Autowired
	public CashFlowService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	public CashFlow userCashFlow(User user, Integer page, Integer size, 
			DateRangeFilter dateRangeFilter, MonthYearFilter monthYearFiler) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("date")));

	    Specification<Transaction> spec = TransactionSpecifications.belongsTo(user);

	    if (dateRangeFilter != null) {
	        spec = spec.and(TransactionSpecifications.byDateRange(dateRangeFilter.getStartDate(), dateRangeFilter.getEndDate()));
	        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
		    return setCashFlowIndicators(transactions);
	    } else if (monthYearFiler != null) {
	        spec = spec.and(TransactionSpecifications.byMonthAndYear(monthYearFiler.getMonth(), monthYearFiler.getYear()));
	        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
		    transactions.forEach(System.out::println);
		    return setCashFlowIndicators(transactions);
	    } else {
	        LocalDate now = LocalDate.now();
	        spec = spec.and(TransactionSpecifications.byMonthAndYear(now.getMonthValue(), now.getYear()));
	        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
		    return setCashFlowIndicators(transactions);
	    }

	}
	
	private CashFlow setCashFlowIndicators(Page<Transaction> transactions) {
		BigDecimal totalRevenue = totalTransactionValue(transactions, TransactionType.REVENUE);
		BigDecimal totalExpense = totalTransactionValue(transactions, TransactionType.EXPENSE);
		BigDecimal result = totalRevenue.subtract(totalExpense);
		BigDecimal biggestRevenue = biggestOf(transactions, TransactionType.REVENUE);
		BigDecimal biggestExpense = biggestOf(transactions, TransactionType.EXPENSE);
		BigDecimal averageRevenue = averageOf(transactions, TransactionType.REVENUE);
		BigDecimal averageExpense = averageOf(transactions, TransactionType.EXPENSE);
		
		
		CashFlow cashFlow = new CashFlow(transactions, 
				totalRevenue, 
				totalExpense, 
				result, 
				biggestRevenue, 
				biggestExpense, 
				averageRevenue, 
				averageExpense);
		
		System.out.println("FluxoService: " + cashFlow);
		
		return cashFlow;
		
	}
	
	private BigDecimal totalTransactionValue(Page<Transaction> transactions, TransactionType criteria) {
		return transactions.stream()
				.filter(t -> t.getTransactionType().equals(criteria))
				.map(Transaction::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private BigDecimal biggestOf(Page<Transaction> transactions, TransactionType criteria) {
	    return transactions.stream()
	            .filter(t -> t.getTransactionType().equals(criteria))
	            .map(Transaction::getValue)
	            .reduce((t1, t2) -> criteria == TransactionType.REVENUE ? t1.max(t2) : t1.min(t2))
	            .orElse(BigDecimal.ZERO);
	}

	
	private BigDecimal averageOf(Page<Transaction> transactions, TransactionType criteria) {
		int size = transactions.stream()
				.filter(t -> t.getTransactionType().equals(criteria))
				.toList()
				.size();
		
		BigDecimal totalTransactionValue = totalTransactionValue(transactions, criteria);
		
		if(size == 0) return BigDecimal.ZERO;
		
		BigDecimal average = totalTransactionValue.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP); 

		return average;
	}
	
	
}
























