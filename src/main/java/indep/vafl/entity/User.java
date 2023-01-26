package indep.vafl.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;



@Entity
@Table(name = "userBase")
public class User extends EntityDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7892418481913055870L;

	@Id
	@SequenceGenerator(name = "seqGenUser", sequenceName = "userBase_userID_seq", initialValue = 1, allocationSize = 3)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGenUser")
	@Column(name = "userID")
	private int id;

	@NotNull
	@Column(name = "userName", unique = true)
	private String userName;

	@NotNull
	@Column(name = "userMail", unique = true)
	private String userMail;

	@NotNull
	@Column(name = "userConfirmation", unique = true)
	private String userConfirmation;

	@NotNull
	@Column(name = "userIsVerified")
	private boolean userIsVerified;
	

	public User() {

	}

	public User(int id, String userName, String userMail, String userConfirmation, boolean userIsVerified) {
		super();
		this.id = id;
		this.userName = userName;
		this.userMail = userMail;
		this.userConfirmation = userConfirmation;
		this.userIsVerified = userIsVerified;
	}

	public User(String userName, String userMail, String userConfirmation, boolean userIsVerified) {
		super();
		this.userName = userName;
		this.userMail = userMail;
		this.userConfirmation = userConfirmation;
		this.userIsVerified = userIsVerified;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", userMail=" + userMail + ", userConfirmation="
				+ userConfirmation + ", userIsVerified=" + userIsVerified + "]";
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
		User other = (User) obj;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getUserConfirmation() {
		return userConfirmation;
	}

	public void setUserConfirmation(String userConfirmation) {
		this.userConfirmation = userConfirmation;
	}

	public boolean isUserIsVerified() {
		return userIsVerified;
	}

	public void setUserIsVerified(boolean userIsVerified) {
		this.userIsVerified = userIsVerified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	};

}
