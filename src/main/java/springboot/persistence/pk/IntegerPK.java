package springboot.persistence.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable 
public class IntegerPK
	implements java.io.Serializable
{
	private static final long serialVersionUID = 4015833785091195351L;
	
	@Column(name="ID")
	private Integer id;
	
	/** default constructor */
	public IntegerPK () {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

}
