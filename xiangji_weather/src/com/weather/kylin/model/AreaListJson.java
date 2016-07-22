package com.weather.kylin.model;

import java.util.List;

public class AreaListJson {
	private String error_code;
	private String reason;
	private List<AreaResult> result;

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<AreaResult> getResult() {
		return result;
	}

	public void setResult(List<AreaResult> result) {
		this.result = result;
	}

}
