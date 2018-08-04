package personal.renamer.exceptions;

@SuppressWarnings("serial")
public abstract class PersonalException extends Exception {
	
	public PersonalException(String msg) {
		super(msg);
	}
	
	@Override
	public abstract String toString();
	
}
