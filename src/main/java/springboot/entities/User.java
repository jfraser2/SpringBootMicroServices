package springboot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name="UserEntity")
@Table( name = "Users",
		indexes = {@Index(name = "IDX_ACTIVE", columnList = "ACTIVE")}
)
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "ID")	
	private Long id;
	@Column (name = "EMAIL", nullable = false)
	private String email;
	@Column (name = "PASSWORD", nullable = false)
    private String password;
	@Column (name = "LAST_NAME", nullable = false)
    private String lastName;
	@Column (name = "FIRST_NAME", nullable = false)
    private String firstName;
	@Column (name = "USER_NAME", nullable = false)
    private String userName;
	@Column (name = "ADDRESS_ONE")
    private String address1;
	@Column (name = "ADDRESS_TWO")
    private String address2;
	@Column (name = "CITY")
    private String city;
	@Column (name = "STATE")
    private String state;
	@Column (name = "ZIP")
    private String zip;
	@Column (name = "ACTIVE")
    private Boolean active;

	public User()
	{
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
