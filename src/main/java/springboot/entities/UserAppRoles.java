package springboot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name="UserAppRolesEntity")
@Table( name = "UserAppRoles",
	uniqueConstraints = @UniqueConstraint(columnNames= {"user_ID", "app_ID", "role_ID"})
)
public class UserAppRoles {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "ID")	
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="user_ID", referencedColumnName="ID")	
	private User user;
	
	@ManyToOne
    @JoinColumn(name="app_ID", referencedColumnName="ID")	
    private App app;
	
	@ManyToOne
    @JoinColumn(name="role_ID", referencedColumnName="ID")
    private Role role;
	
	public UserAppRoles()
	{
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
