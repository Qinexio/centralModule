package indep.vafl.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sentence")
public class Sentence extends EntityDate implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7892418481913055870L;

	@Id
	@SequenceGenerator(name = "seqGenSentence", sequenceName = "sentence_sentenceID_seq", initialValue = 1, allocationSize = 3)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGenSentence")
	@Column(name = "sentenceID")
	private long id;

	@NotNull
	@Column(name = "sentenceText")
	private String sentenceText;

	@NotNull
	@Column(name = "sentencePrediction")
	private String sentencePrediction;
	
	@Column(name = "sentencePercentages")
	private String sentencePercentages;
	
	@NotNull
	@Column(name = "sentenceIsVerified")
	private boolean sentenceIsVerified;
	
	@Column(name = "sentenceUserInput")
	private boolean sentenceUserInput;
	
	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "batchIDfk",  referencedColumnName = "batchID")
	@OnDelete(action = OnDeleteAction.CASCADE)
    private Batch sentenceBatch;
	
	public Sentence() {

	}

	public Sentence(int id, @NotNull String sentenceText, @NotNull String sentencePrediction,
			@NotNull boolean sentenceIsVerified, Boolean sentenceUserInput) {
		super();
		this.id = id;
		this.sentenceText = sentenceText;
		this.sentencePrediction = sentencePrediction;
		this.sentenceIsVerified = sentenceIsVerified;
		this.sentenceUserInput = sentenceUserInput;
	}

	@Override
	public String toString() {
		return "Sentence [id=" + id + ", sentenceText=" + sentenceText + ", sentencePrediction=" + sentencePrediction
				+ ", sentenceIsVerified=" + sentenceIsVerified + ", sentenceUserInput=" + sentenceUserInput + "]";
	}

	@Override
	public int hashCode() {
		final long prime = 31;
		int result = 1;
		result = (int) (prime * result + id);
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
		
		Sentence other = (Sentence) obj;
		return (sentencePrediction == other.sentencePrediction)&&(sentenceText == other.sentenceText);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSentenceText() {
		return sentenceText;
	}

	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}

	public String getSentencePrediction() {
		return sentencePrediction;
	}

	public void setSentencePrediction(String sentencePrediction) {
		this.sentencePrediction = sentencePrediction;
	}

	public boolean isSentenceIsVerified() {
		return sentenceIsVerified;
	}

	public void setSentenceIsVerified(boolean sentenceIsVerified) {
		this.sentenceIsVerified = sentenceIsVerified;
	}

	public Boolean getSentenceUserInput() {
		return sentenceUserInput;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Batch getSentenceBatch() {
		return sentenceBatch;
	}

	public void setSentenceBatch(Batch sentenceBatch) {
		this.sentenceBatch = sentenceBatch;
	}

	public String getSentencePercentages() {
		return sentencePercentages;
	}

	public void setSentencePercentages(String sentencePercentages) {
		this.sentencePercentages = sentencePercentages;
	}

	public void setSentenceUserInput(boolean sentenceUserInput) {
		this.sentenceUserInput = sentenceUserInput;
	}
	
}
