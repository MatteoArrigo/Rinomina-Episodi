package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class NoExtensionException extends PersonalException{
	
	public NoExtensionException() {
		super("Problema con il nome del file.");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": l'estensione non è presente.";
	}
}
