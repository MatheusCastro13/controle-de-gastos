package matheusresio.controle_de_gastos.model.enums;

public enum TransactionType {

	EXPENSE("EXPENSE"), 
	REVENUE("REVENUE");
	
	private String name;
	
	TransactionType(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
