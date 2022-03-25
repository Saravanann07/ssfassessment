package vttp2022.ssf.Assessment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import vttp2022.ssf.Assessment.models.Quotation;
import vttp2022.ssf.Assessment.services.QuotationService;

@SpringBootTest
@AutoConfigureMockMvc
class AssessmentApplicationTests {

	@Autowired
	private QuotationService quoSvc;
	
	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
		List<String> items = new ArrayList<>();
		items.add("durian");
		items.add("plum");
		items.add("pear");

		Optional<Quotation> opt = quoSvc.getQuotations(items);
		assertTrue(opt.isEmpty());
		

	}			
}
