package indep.vafl.utility;

import java.io.Serializable;
import java.util.Objects;

public class SentenceDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sentenceText;

	private String sentencePrediction;

	
	public SentenceDTO() {
		super();
	}
	
	

	public SentenceDTO(String sentenceText, String sentencePrediction) {
		super();
		this.sentenceText = sentenceText;
		this.sentencePrediction = sentencePrediction;
	}

	@Override
	public String toString() {
		return "SentenceDTO [sentenceText=" + sentenceText + ", sentencePrediction=" + sentencePrediction + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(sentencePrediction, sentenceText);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SentenceDTO other = (SentenceDTO) obj;
		return Objects.equals(sentencePrediction, other.sentencePrediction)
				&& Objects.equals(sentenceText, other.sentenceText);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
