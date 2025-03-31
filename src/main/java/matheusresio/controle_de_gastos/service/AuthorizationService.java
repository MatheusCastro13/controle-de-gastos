package matheusresio.controle_de_gastos.service;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.model.User;

public interface AuthorizationService {
    void verifyUserAccess(User userAuthenticated, UUID targetUserId) throws AccessDeniedException, EntityNotFoundException;
}
