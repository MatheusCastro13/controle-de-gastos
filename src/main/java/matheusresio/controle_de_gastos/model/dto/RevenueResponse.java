package matheusresio.controle_de_gastos.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public record RevenueResponse(Long id, Date date, String description, BigDecimal value) {

}
