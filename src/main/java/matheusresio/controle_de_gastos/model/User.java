package matheusresio.controle_de_gastos.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matheusresio.controle_de_gastos.model.dto.LoginRequest;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false, unique = true, length = 50)
	private String username;
	
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Instant creatinDate;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Revenue> revenues = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Expense> expenses = new ArrayList<>();
	
	public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(loginRequest.password(), this.password);
	}
	
	public void addRevenue(Revenue revenue) {
		if(revenue != null && !revenues.contains(revenue)) {
			revenues.add(revenue);
			if(revenue.getUser() != this) {
				revenue.setUser(this);
			}
		}
	}

	public void removeRevenue(Revenue revenue) {
		if(revenues.remove(revenue)) {
			if(revenue.getUser() == this) {
				revenue.setUser(null);
			}
		}
	}
	
	public void addExpense(Expense expense) {
		if(expense != null && !expenses.contains(expense)) {
			expenses.add(expense);
			if(expense.getUser() != this) {
				expense.setUser(this);
			}
		}
	}

	public void removeExpense(Expense expense) {
		if(expenses.remove(expense)) {
			if(expense.getUser() == this) {
				expense.setUser(null);
			}
		}
	}
	
	@Override
	public String toString() {
		return  "id: " + this.id +
				"\nusername: " + this.username +
				"\nemail: " + this.email +
				"\ncriado em: " + this.creatinDate +
				"\nrole: " + this.role.getName();
				
	}
}













