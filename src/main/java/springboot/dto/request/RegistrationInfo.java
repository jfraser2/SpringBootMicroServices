package springboot.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegistrationInfo {
	@NotBlank
	@Email
	private String email;
	@NotBlank
    private String password;
	@NotBlank
    private String lastName;
	@NotBlank
    private String firstName;
	@NotBlank
    private String userName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
	@NotBlank
	private String regAppName;
    
    public RegistrationInfo()
    {
    }
    
	public RegistrationInfo(String email, String password, String lastName, String firstName,
		String userName, String address1, String address2, String city, String state,
		String zip, String regAppName)
	{
		this.email = email;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		this.userName = userName;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.regAppName = regAppName;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getAddress1() {
		return address1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	public String getAddress2() {
		return address2;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getRegAppName() {
		return regAppName;
	}

	public void setRegAppName(String regAppName) {
		this.regAppName = regAppName;
	}
    
}

