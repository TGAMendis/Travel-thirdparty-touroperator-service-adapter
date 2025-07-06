package travel-thirdparty-touroperator-service-adapter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel-thirdparty-touroperator-service-adapter.processor.ActivityRequestResponseProcessor;

@RestController
@RequestMapping("/api/activity")
public class ActivityRequestController {
	@Autowired
	ActivityRequestResponseProcessor activityProcessor;
	private static Logger logger = LoggerFactory.getLogger(ActivityRequestController.class);

	@PostMapping("/getResponse")
	public String getSupplierResponse(@RequestBody String supplierRequest) {
		
		logger.info("ActivityRequestController  getSupplierResponse started");
		long smili = System.currentTimeMillis();
		
		String supResponse = activityProcessor.processSupplierResponse(supplierRequest);
		
		double convertStreamToStringTime = (System.currentTimeMillis() - smili) / 1000.0;
		logger.info("activityProcessor.processSupplierResponse took : " + convertStreamToStringTime + "s.");

		return supResponse;

	}

}
