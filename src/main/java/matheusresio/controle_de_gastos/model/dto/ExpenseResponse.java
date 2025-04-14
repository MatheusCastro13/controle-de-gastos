package matheusresio.controle_de_gastos.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(Long id, LocalDate date, String description, BigDecimal value) {

}
