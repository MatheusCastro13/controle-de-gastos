package matheusresio.controle_de_gastos.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.exceptions.SameCredentialsException;
import matheusresio.controle_de_gastos.exceptions.SamePasswordException;
import matheusresio.controle_de_gastos.exceptions.WrongPasswordException;
import matheusresio.controle_de_gastos.model.Role;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.PasswordChange;
import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.model.dto.UserRegisterAdm;
import matheusresio.controle_de_gastos.model.dto.UserUpdateDto;
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
		Optional<User> user = userRepository.findByEmail(email);
		return user;

	}

	@Transactional
	public void save(UserRegister userRegister) throws CredentialsAlreadyUsedException {
		Role role = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));

		if (!validCredentials(userRegister.username(), userRegister.email())) {
			throw new CredentialsAlreadyUsedException("Username ou email já utilizado");
		}

		User user = new User();
		user.setUsername(userRegister.username());
		user.setEmail(userRegister.email());
		user.setPassword(passwordEncoder.encode(userRegister.password()));
		user.setRole(role);

		userRepository.save(user);
	}

	@Transactional
	public void save(UserRegisterAdm userDto) throws CredentialsAlreadyUsedException {
		System.out.println("userDto.role() = " + userDto.role());
		Role role = roleRepository.findByName("ROLE_" + userDto.role())
				.orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));

		if (!validCredentials(userDto.username(), userDto.email())) {
			System.out.println("credenciais invalidas");
			throw new CredentialsAlreadyUsedException("Username ou email já utilizado");
		}

		User user = new User();
		user.setUsername(userDto.username());
		user.setEmail(userDto.email());
		user.setPassword(passwordEncoder.encode(userDto.password()));
		user.setRole(role);
		System.out.println("user a ser salvo: " + user);
		userRepository.save(user);
	}

	private void verifyCredentials(String username, String email) throws CredentialsAlreadyUsedException{
		Optional<User> userByUsername = userRepository.findByUsername(username);
		Optional<User> userByEmail = userRepository.findByEmail(email);

		if (userByEmail.isPresent() || userByUsername.isPresent()) {
			throw new CredentialsAlreadyUsedException("O username ou email já esta sendo utilizado por outro usuario");
		}
	}
	
	private boolean validCredentials(String username, String email) {
		Optional<User> userByUsername = userRepository.findByUsername(username);
		Optional<User> userByEmail = userRepository.findByEmail(email);

		if (userByEmail.isPresent() || userByUsername.isPresent()) {
			return false;
		}

		return true;
	}

	public void verifyPermissions(User user, UUID id) throws AccessDeniedException, EntityNotFoundException {
		if (!verifyIdExists(id)) {
			throw new EntityNotFoundException("User com id: " + id + "não foi encontrado");
		}

		if (user.getRole().getName().equals("ROLE_USER")) {
			if (!verifyUserAuthenticatedId(id, user)) {
				throw new AccessDeniedException("Você não tem permissão para ver os dados desse usuário");
			}
		}

	}

	@Transactional
	public void changePassword(UUID id, User user, PasswordChange password) throws SamePasswordException, WrongPasswordException {
		this.verifyPermissions(user, id);
		this.verifyPassword(user, password);
		user.setPassword(passwordEncoder.encode(password.getNewPassword()));

	}
	
	private void verifyPassword(User user, PasswordChange password) throws SamePasswordException, WrongPasswordException {
		if(!passwordEncoder.matches(password.getCurrentPassword(), user.getPassword())){
			throw new WrongPasswordException("A senha atual não confere");
		}
		
		if(passwordEncoder.matches(password.getNewPassword(), user.getPassword())) {
			throw new SamePasswordException("As senhas são idênticas");
		}
	}

	private boolean verifyUserAuthenticatedId(UUID id, User user) {
		return user.getId().equals(id);
	}

	private boolean verifyIdExists(UUID id) {
		return !userRepository.findById(id).isPresent();

	}

	@Transactional
	public void update(UUID id, User user, UserUpdateDto userDto) throws CredentialsAlreadyUsedException, AccessDeniedException, SameCredentialsException {
		this.verifyPermissions(user, id);
		this.verifyUserIsUpdateble(user, userDto.getUsername(), userDto.getEmail());
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
	}

	private void verifyUserIsUpdateble(User user, String username, String email) throws SameCredentialsException, CredentialsAlreadyUsedException {
		if(user.getUsername().equals(username) && user.getEmail().equals(email)) {
			throw new SameCredentialsException("Não é possível atualizar com os mesmos dados");
		}
		
		Optional<User> userByUsername = userRepository.findByUsername(username);
		Optional<User> userByEmail = userRepository.findByEmail(email);

		if (userByEmail.isPresent()) {
			if(!userByEmail.get().equals(user)) {
				throw new CredentialsAlreadyUsedException("O email já esta sendo utilizado por outro usuario");
			}
		}
		
		if (userByUsername.isPresent()) {
			if(!userByUsername.get().equals(user)) {
				throw new CredentialsAlreadyUsedException("O username já esta sendo utilizado por outro usuario");
			}
		}
	}

	public User findById(UUID id) {
		return userRepository.findById(id).get();
	}
}














