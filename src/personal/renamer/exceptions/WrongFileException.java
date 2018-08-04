package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class WrongFileException extends PersonalException{
	
	public WrongFileException() {
		super("Problema col file");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": il file non ha estensione è la cartella dei sottotitoli.";
	}

}
