package matheusresio.controle_de_gastos.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.model.Role;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.repository.RoleRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, 
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<User> findByEmail(String email) {
		Optional<User> user =  userRepository.findByEmail(email);
		return user;
		
	}

	@Transactional
	public void save(UserRegister userRegister) {
		Role role = roleRepository.findByName("ROLE_USER");
		
		User user = new User();
		user.setUsername(userRegister.username());
		user.setEmail(userRegister.email());
		user.setPassword(passwordEncoder.encode(userRegister.password()));
		user.setRole(role);
		
		userRepository.save(user);
	}
	
}
