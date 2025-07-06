package travel.thirdparty.touroperator.service.adapter.processor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import travel.thirdparty.touroperator.service.adapter.controller.ActivityRequestController;
import travel.thirdparty.touroperator.service.dto.SupplierIntermediateData;

@Service
public class ActivityRequestResponseProcessor {

	private static Logger logger = LoggerFactory.getLogger(ActivityRequestResponseProcessor.class);

	public String processSupplierResponse(String jsonRequest) {
		SupplierIntermediateData supIntData;
		String jsonResponse="";
		supIntData = new SupplierIntermediateData();
		supIntData = (SupplierIntermediateData) populateObjectFromJsonString(jsonRequest, supIntData);

		try {
			send(supIntData);
			jsonResponse=populateJsonStringFromObject(supIntData);
		} catch (Exception e) {
			logger.error("processSupplierResponse : ",e);
		}
		logger.info("RequestID: " + supIntData.getRequestId()+ " , SupplierResponse : "+jsonResponse);
		return jsonResponse;
	}

	public String populateJsonStringFromObject(Object obj) {
		ObjectMapper mapper = null;
		String jsonString = null;

		try {

			mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			jsonString = mapper.writeValueAsString(obj);

		} catch (Exception e) {
			logger.error("Error while converting object to json string:", e);
		}

		return jsonString;
	}

	public Object populateObjectFromJsonString(String jsonStr, Object obj) {
		ObjectMapper mapper = null;
		Object data = null;

		try {

			mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			data = mapper.readValue(jsonStr, obj.getClass());

		} catch (Exception e) {
			logger.error("Error while converting json string to object:",e);
		}
		return data;
	}

	public void send(SupplierIntermediateData requestInfo) throws Exception {
		ByteArrayOutputStream baos = null;
		URL mUrl = null;

		HttpsURLConnection httpConn = null;
		String supplierRequest = requestInfo.getSupplierRequest();
		logger.info("supplierRequest::"+supplierRequest);
		logger.info("requestInfo.getUrl()::"+requestInfo.getUrl());
		String supplierResponse;
		InputStream responseInputStream = null;
		try {
			baos = new ByteArrayOutputStream();
			mUrl = new URL(requestInfo.getUrl());
			
			httpConn = (HttpsURLConnection) mUrl.openConnection();
			httpConn.setRequestMethod(requestInfo.getHttpMethod());
			httpConn.setDoOutput(true);
			httpConn.setUseCaches(false);
			httpConn.setDoInput(true);

			setDefaultConnectionTimeOut(httpConn);

			for (Map.Entry<String, String> entry : requestInfo.getAdditionalProperties().entrySet()) {
				httpConn.setRequestProperty(entry.getKey(), entry.getValue());
			}

			if (supplierRequest != null && !"".equals(supplierRequest.trim())) {
				baos.write(supplierRequest.toString().trim().getBytes());
				baos.writeTo(httpConn.getOutputStream());
			}

			if (httpConn.getResponseCode() >= 300) {
				supplierResponse=convertStreamToString(httpConn.getErrorStream());
				logger.info("Supplier Error Response : " + supplierResponse);
				requestInfo.setSupplierResponse(supplierResponse);
				
				throw new IOException("Did not receive successful HTTP response: status code = "
						+ httpConn.getResponseCode() + ", status message = [" + httpConn.getResponseMessage() + "]");
			}

			responseInputStream = httpConn.getInputStream();

			supplierResponse=convertStreamToString(responseInputStream);
			requestInfo.setSupplierResponse(supplierResponse);

		} catch (Exception e) {
			// throw e;
			logger.error("ActivityCommonProcessor Send  : ",e);
		}

	}

	private void setDefaultConnectionTimeOut(HttpsURLConnection con) {
		con.setConnectTimeout(45000);
		con.setReadTimeout(30000);
	}

	public String convertStreamToString(InputStream inputStream) throws Exception {

		long smili = System.currentTimeMillis();

		logger.info("Start convert Stream To String");
		StringBuilder sb;

		if (inputStream != null) {

			sb = new StringBuilder();

			for (int c; (c = inputStream.read()) >= 0;) {
				sb.append((char) c);
			}
			logger.info("convertStreamToString--------------------------->" + sb.toString());
			long emili = System.currentTimeMillis();
			double convertStreamToStringTime = (emili - smili) / 1000.0;

			logger.info("End convertStreamToString took " + convertStreamToStringTime + "s.");
			return sb.toString();
		}

		long emili = System.currentTimeMillis();
		double convertStreamToStringTime = (emili - smili) / 1000.0;

		logger.info("End convertStreamToString took " + convertStreamToStringTime + "s.");
		return null;
	}

}
