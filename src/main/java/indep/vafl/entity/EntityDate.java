package indep.vafl.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

//this class allows the persistence manager to add creation and update dates to entities

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"dateCreate", "dateUpdate"},
        allowGetters = true
)
public abstract class EntityDate implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateCreate", nullable = false, updatable = false)
    @CreatedDate
    private Date dateCreate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateUpdate", nullable = false)
    @LastModifiedDate
    private Date dateUpdate;
	
    public EntityDate() {
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}



  
}