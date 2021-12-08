package com.examples.gatewaydemo.httputils;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class HResponse {
	private static final int NO_ERROR = -1;
	private static final int NO_ERROR_MIN_CODE	= 400;
	public InputStream inputStream;
//	public byte[] xmlByte;
	public String xmlString;
	public JSONObject dataJsonObject;
	public JSONArray dataJsonArray;
	public boolean dowloadFile = false;
	public int errorCode = NO_ERROR;
	public int responseCode;
	
	protected void setResponseCode(int responseCode){
		this.responseCode = responseCode;
		if(responseCode >= NO_ERROR_MIN_CODE)
			errorCode = responseCode;
	}
	
	protected boolean haveError(){
		return errorCode != NO_ERROR;
	}

	public boolean haveArrayError(){
		return false;
	}
}
