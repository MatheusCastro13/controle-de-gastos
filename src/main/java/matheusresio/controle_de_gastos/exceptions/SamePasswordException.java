package matheusresio.controle_de_gastos.exceptions;

public class SamePasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SamePasswordException(String message) {
		super(message);
	}

}
