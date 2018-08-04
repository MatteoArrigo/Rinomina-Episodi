package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class NotAcceptableExtensionException extends PersonalException{
	
	public NotAcceptableExtensionException() {
		super("Problema con l'estensione del file.");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": l'estensione del file non è riconosciuta.";
	}
}
