package springboot.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AdminUser {
	@NotNull
	private Long id;
	@NotBlank
	private String appName;

	public AdminUser()
	{
	}
	
	public AdminUser(Long anId)
	{
		this.id = anId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
}
