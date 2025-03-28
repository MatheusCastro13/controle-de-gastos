package matheusresio.controle_de_gastos.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CashFlow {

	private List<Transaction> transactions = new ArrayList<>();
	private BigDecimal totalRevenue;
	private BigDecimal totalExpense;
	private BigDecimal result;
	private BigDecimal biggestRevenue;
	private BigDecimal biggestExpense;
	private BigDecimal averageRevenue;
	private BigDecimal averageExpense;
	
	
}
