package indep.vafl.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "userProfile")
public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3088271801332671258L;

	@Id
	@SequenceGenerator(name="seqGenProfile", sequenceName = "userProfile_extID_seq", initialValue=1, allocationSize=3)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqGenProfile")
	@Column(name = "extID")
	private int id;

	@NotNull
	@Column(name = "extName")
	private String profileName;
	
	@NotNull 
	@Column(name = "extRole")
	private String profileRole;
	
	@Column(name = "extIsBanned") 
	private boolean profileIsBanned;

	@OneToOne(optional = false, fetch = FetchType.LAZY,orphanRemoval=true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "userIDfk", referencedColumnName = "userID")
	@JsonIgnore
	private User profileUser;

	public Profile() {

	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (id != other.id)
			return false;
		return true;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isProfileIsBanned() {
		return profileIsBanned;
	}


	public void setProfileIsBanned(boolean profileIsBanned) {
		this.profileIsBanned = profileIsBanned;
	}

	public String getProfileRole() {
		return profileRole;
	}


	public void setProfileRole(String profileRole) {
		this.profileRole = profileRole;
	}

	public User getProfileUser() {
		return profileUser;
	}

	public void setProfileUser(User profileUser) {
		this.profileUser = profileUser;
	}

	public String getProfileName() {
		return profileName;
	}


	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	};

}
