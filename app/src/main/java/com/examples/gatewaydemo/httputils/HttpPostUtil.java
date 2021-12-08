package com.examples.gatewaydemo.httputils;

import java.io.IOException;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HttpPostUtil {
	private void HttpPostRequest(String key1,String key2,String url){
		OkHttpClient mOkHttpClient=new OkHttpClient();
		FormEncodingBuilder builder=new FormEncodingBuilder();
		builder.add("username", key1);
		builder.add("password", key2);
		
		final Request request=new Request.Builder().url(url).post(builder.build()).build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				String str=request.body().toString();
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
