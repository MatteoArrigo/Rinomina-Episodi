package personal.renamer.exceptions;

@SuppressWarnings("serial")
public class StrangeThingException extends PersonalException {
	
	public StrangeThingException() {
		super("Problema con il titolo.");
	}
	
	@Override
	public String toString() {
		return getMessage() + ": parte del titolo strana trovata.";
	}
}
