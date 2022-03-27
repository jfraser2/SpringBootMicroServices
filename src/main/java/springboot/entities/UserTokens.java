package springboot.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="UserTokensEntity")
@Table( name = "UserTokens")
public class UserTokens {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "ID")	
	private Long id;
	
	/**
	 *
	 * Inactivate all tokens for a user
	 * My laptop is in the back of a taxi cab in NYC. Lol
	 * I will never see that again.
	 * 
	*/
	@ManyToOne
    @JoinColumn(name="user_ID", referencedColumnName="ID", nullable = false)	
	private User user;
	
	/**
	 *
	 * Inactivate all tokens for an application
	 * We have been hacked and I don't know which userId 
	 * The hacker is using yet.
	 * 
	*/
	@ManyToOne
    @JoinColumn(name="app_ID", referencedColumnName="ID")	
    private App app;
	
	/**
	 *
	 * Inactivate all tokens that should have expired by Now
	 * Where In the world did Carmen go on vacation? Lol 
	 * Weekly or Nightly Process
	 * 
	*/
	@Column (name = "CREATED_TIMESTAMP", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdTimestamp;
	
	@Column (name = "ACTIVE", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean active;
	
	public UserTokens()
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

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
