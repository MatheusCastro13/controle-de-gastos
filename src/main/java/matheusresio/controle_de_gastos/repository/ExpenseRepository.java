package matheusresio.controle_de_gastos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

	Page<Expense> findAllByUser(User user, Pageable pageable);


}
