package com.examples.gatewaydemo.httputils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.examples.gatewaydemo.util.LogUtils;
import com.examples.gatewaydemo.util.Tool;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpGetUtil {
	private Context context;
	public HttpGetUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	public void HttpGetRequest(String url,final Handler handler,final int SuccessFlag,final int FailureFlag){
		OkHttpClient mOkHttpClient=new OkHttpClient();
		final Request request=new Request.Builder().url(url).build();
		Call call=mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				
				String tmpBody = null;
                try {
                    tmpBody = Tool.getJsonString(arg0.body().byteStream(), "UTF-8");
//                    tmpBody = arg0.body().string();
                    //LogUtils.v("tmpBody==="+tmpBody);

                    if (tmpBody.contains("[]"))
                        handler.sendEmptyMessage(-100);
                    
                    Message msg=new Message();
    				msg.what=SuccessFlag;
    				msg.obj=tmpBody;
    				handler.sendMessage(msg);
                } catch (Exception e) {
//                    e.printStackTrace();
//                    Message msg=new Message();
//    				msg.what=FailureFlag;
//    				handler.sendMessage(msg);
                }
				
				
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				try {
					String str=arg0.body().toString();
					String  error=arg1.toString();
					LogUtils.d("错误信息："+error);
				}catch (Exception e){

				}
				handler.sendEmptyMessage(FailureFlag);
			}
		});
	}
}
