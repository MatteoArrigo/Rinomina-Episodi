package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class URLNotFoundException extends PersonalException {
	
	public URLNotFoundException() {
		super("Problema con l'url");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": url non trovato.";
	}
	
}
