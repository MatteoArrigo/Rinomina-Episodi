package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class LanguageException extends PersonalException{
	
	public LanguageException() {
		super("Problema con le lingue dei sottotitoli.");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": sono state trovate due lingue identiche.";
	}

}
