package indep.vafl.pages;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import indep.vafl.datarepo.BatchRepository;
import indep.vafl.entity.Batch;

@Controller
public class BatchPage {

	@Autowired
	BatchRepository batchRepository;

	@Secured("ROLE_USER")
	@GetMapping("/showModBatch")
	public String getBatchesUser(@PageableDefault(size = 10) Pageable pageable, Model model) {
		
		Page<Batch> page = batchRepository.findByBatchComplete(false, pageable);
		model.addAttribute("page", page);
		return "showModBatch.html";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/showAdminBatch")
	public String getBatchesAdmin(@PageableDefault(size = 10) Pageable pageable, Model model) {
		
		Page<Batch> page = batchRepository.findAll(pageable);
		model.addAttribute("page", page);
		return "showAdminBatch.html";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/showAdminBatchEdit/{batchID}")
	public String getSentencesAdmin(@PathVariable(value = "batchID") Integer batchID, Model model) {
		
		Optional<Batch> obj = batchRepository.findById(batchID);
		model.addAttribute("obj", obj.get());
		return "showAdminBatchEdit.html";
	}

}