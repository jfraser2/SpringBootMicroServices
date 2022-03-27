package springboot.dto.response;

public class AdminOperation {
	
	private String message;
	
	public AdminOperation(String aMessage)
	{
		this.message = aMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
