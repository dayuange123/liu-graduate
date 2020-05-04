
package com.ljy.graduate.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> implements Serializable {

	private int ec = 200;

	private String em = "success";

	private T data;

	public Response(T data) {
		this.data = data;
	}

	public Response(ResponseMessage responseMsg) {
		setEc(responseMsg.getEc());
		setEm(responseMsg.getEm());
	}

	public Response(ResponseMessage responseMsg, T data) {
		this.ec = responseMsg.getEc();
		this.em = responseMsg.getEm();
		this.data = data;
	}

	public void build(ResponseMessage responseMsg, T t) {
		build(responseMsg);
		setData(t);
	}

	public void build(ResponseMessage responseMsg) {
		setEc(responseMsg.getEc());
		setEm(responseMsg.getEm());
	}

	public Response(int ec, String em) {
		this.ec = ec;
		this.em = em;
	}

	public Response(int ec, String em, T data) {
		this.ec = ec;
		this.em = em;
		this.data = data;
	}

	public static boolean isSuccess(Response response) {
		return null != response && 200 == response.getEc();
	}


}
