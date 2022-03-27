package springboot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name="RoleEntity")
@Table( name = "Roles",
		uniqueConstraints = @UniqueConstraint(columnNames= {"ROLE_NAME"})
)
public class Role {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "ID")	
	private Long id;
	
	@Column (name = "ROLE_NAME", nullable = false)
	private String roleName;

	public Role()
	{
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
