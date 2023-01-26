package indep.vafl.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import indep.vafl.entity.Batch;

public interface TrainService {
	public Boolean trainModel(List<Batch> toSend, Boolean useCSV, Boolean useGloVE) throws JsonParseException, JsonMappingException, IOException;
	public Boolean reloadModel();
}
