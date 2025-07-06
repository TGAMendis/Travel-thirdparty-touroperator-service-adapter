package travel.thirdparty.touroperator.service.adapter.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "requestId", "url", "httpMethod", "supplierRequest", "supplierResponse" })
public class SupplierIntermediateData {

	@JsonProperty("requestId")
	private String requestId;
	@JsonProperty("url")
	private String url;
	@JsonProperty("httpMethod")
	private String httpMethod;
	@JsonProperty("supplierRequest")
	private String supplierRequest;
	@JsonProperty("supplierResponse")
	private String supplierResponse;
	@JsonIgnore
	private Map<String, String> additionalProperties = new HashMap<String, String>();

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSupplierRequest() {
		return supplierRequest;
	}

	public void setSupplierRequest(String supplierRequest) {
		this.supplierRequest = supplierRequest;
	}

	public String getSupplierResponse() {
		return supplierResponse;
	}

	public void setSupplierResponse(String supplierResponse) {
		this.supplierResponse = supplierResponse;
	}

	public void setAdditionalProperties(Map<String, String> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@JsonProperty("httpMethod")
	public String getHttpMethod() {
		return httpMethod;
	}

	@JsonProperty("httpMethod")
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	@JsonAnyGetter
	public Map<String, String> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, String value) {
		this.additionalProperties.put(name, value);
	}

}
