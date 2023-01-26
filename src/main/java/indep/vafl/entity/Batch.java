package indep.vafl.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "batch")
public class Batch extends EntityDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 799815314173249899L;
	
	@Id
	@SequenceGenerator(name = "seqGenBatch", sequenceName = "batchBase_baseID_seq", initialValue = 1, allocationSize = 3)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGenBatch")
	@Column(name = "batchID")
	private int id;
	
	@NotNull
	@Column(name = "batchComplete")
	private boolean batchComplete;
	
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Sentence> batchSentences;
    
	public Batch()
	{
		
	}
	
	public Batch(int id, @NotNull boolean batchComplete) {
		super();
		this.id = id;
		this.batchComplete = batchComplete;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isBatchComplete() {
		return batchComplete;
	}

	public void setBatchComplete(boolean batchComplete) {
		this.batchComplete = batchComplete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public Set<Sentence> getBatchSentences() {
		return batchSentences;
	}

	public void setBatchSentences(Set<Sentence> batchSentences) {
		this.batchSentences = batchSentences;
	}
	
	public void addBatchSentences(Sentence sentence) {
		this.batchSentences.add(sentence);
	}
	
	public void addBatchSentences(Collection<Sentence> sentences) {
		this.batchSentences.addAll(sentences);
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
		Batch other = (Batch) obj;
		return id == other.id;
	}
	
	
}
