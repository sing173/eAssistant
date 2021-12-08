package com.examples.gatewaydemo.httputils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class HttpDownloadUtil {
	//单一文件下载
	public void downAsynFile() {
		OkHttpClient mOkHttpClient=new OkHttpClient();
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder().url(url).build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response response) throws IOException {
				// TODO Auto-generated method stub
				 InputStream inputStream = response.body().byteStream();
	                FileOutputStream fileOutputStream = null;
	                try {
	                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
	                    byte[] buffer = new byte[2048];
	                    int len = 0;
	                    while ((len = inputStream.read(buffer)) != -1) {
	                        fileOutputStream.write(buffer, 0, len);
	                    }
	                    fileOutputStream.flush();
	                } catch (IOException e) {
	                    Log.i("wangshu", "IOException");
	                    e.printStackTrace();
	                }

	                Log.d("wangshu", "文件下载成功");
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
    }
}
