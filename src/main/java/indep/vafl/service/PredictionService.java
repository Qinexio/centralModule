package indep.vafl.service;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import indep.vafl.entity.Sentence;

public interface PredictionService {
	Set<Sentence> predictText(String toPredict) throws JsonParseException, JsonMappingException, IOException;
}
