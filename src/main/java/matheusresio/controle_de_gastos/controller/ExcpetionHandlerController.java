package matheusresio.controle_de_gastos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExcpetionHandlerController {

	 private static final Logger logger = LoggerFactory.getLogger(ExcpetionHandlerController.class);

	    @ExceptionHandler({ AccessDeniedException.class, EntityNotFoundException.class })
	    public String handleAccessDenied(Exception ex, Model model) { 
	        logger.error("Erro de autorização ou não encontrado: ", ex);
	        
	        model.addAttribute("error", ex.getMessage()); 
	        return "access-denied"; 
	    }
	  
}
