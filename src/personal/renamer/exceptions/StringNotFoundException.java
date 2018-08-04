package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class StringNotFoundException extends PersonalException {
	
	public StringNotFoundException() {
		super("Problema con la ricerca della Stringa.");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": string non trovata.";
	}

}
