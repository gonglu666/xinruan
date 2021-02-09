package com.example.xinran.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlResponse implements Serializable {
	private static final long serialVersionUID = -6316712921739341934L;

	@JsonProperty("status")
	private String status;

	@JsonProperty("msg")
	private String msg;

	@JsonProperty("data")
	private Object data;

	public static GlResponse getResponse(String status,String message,Object data){
		GlResponse r = new GlResponse();
		r.status = status;
		r.msg = message;
		r.data = data;
		return r;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return msg;
	}

	public void setMessage(String message) {
		this.msg = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isStatusSuccess() {
		return (0 == getStatus().compareToIgnoreCase("SUCCESS"));
	}
}