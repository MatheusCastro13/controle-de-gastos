package matheusresio.controle_de_gastos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import matheusresio.controle_de_gastos.model.dto.Transaction;
import matheusresio.controle_de_gastos.model.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

	Optional<Transaction> findByTransactionTypeIdAndTransactionType(Long id, TransactionType type);
}
