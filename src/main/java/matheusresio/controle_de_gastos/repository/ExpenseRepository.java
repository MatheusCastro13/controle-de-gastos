package matheusresio.controle_de_gastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import matheusresio.controle_de_gastos.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
