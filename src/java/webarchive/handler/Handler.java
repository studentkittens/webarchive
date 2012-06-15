package webarchive.handler;

public abstract class Handler {

	public String getName() {
		return getClass().getSimpleName();
	}
	
}
