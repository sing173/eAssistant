package com.examples.gatewaydemo.httputils;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.examples.gatewaydemo.util.LogUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUploadUtil {

	public void HttpUploadRequest(){
		OkHttpClient mOkHttpClient=new OkHttpClient();
		File file = new File(Environment.getExternalStorageDirectory(), "balabala.mp4");

		RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

		RequestBody requestBody = new MultipartBuilder()
		     .type(MultipartBuilder.FORM).addFormDataPart("txt", "text.text", fileBody)
		     .build();

		Request request = new Request.Builder()
		    .url("http://192.168.1.103:8080/okHttpServer/fileUpload")
		    .post(requestBody)
		    .build();

		Call call = mOkHttpClient.newCall(request);

		call.enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void HttpUploadRequest(final Activity activity, String actionUrl, String filePath){

		OkHttpClient mOkHttpClient=new OkHttpClient();
//		mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);//设置超时时间
//			mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);//设置读取超时时间
//			mOkHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);//设置写入超时时间

		File file=new File(filePath);
		if(!file.exists()){
			LogUtils.d("上传文件不存在");
			return;
		}
		String boundary = "******";
		//application/octet-stream  multipart/form-data
		//MediaType type=MediaType.parse()
		String type="multipart/form-data;boundary=123";
		RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM).addFormDataPart("txt", "text.text", fileBody)
				.build();
		final Request request=new Request.Builder().addHeader("Connection", "close").url(actionUrl).post(fileBody).build();
		if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
			//request.addHeader("Connection", "close");
		}
		final Call call=mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Response response) throws IOException {
				// TODO Auto-generated method stub
				final String s=response.body().toString();
				Log.d("flag", "----------------->onResponse: " +response.body().string());
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(activity, "文件上传成功"+s, Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				final String fail=arg0.body().toString();
				LogUtils.d("上传文件失败"+arg1.toString());
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(activity, "文件上传失败"+fail, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	public static String sendFile(String urlPath, String filePath,
            String newName,OnUploadListener listener) throws Exception {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";

        URL url = new URL(urlPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //下载需要将setDoInput方法的参数值设为true
        //con.setDoInput(true);
        //上传需要将setDoOutput方法的参数值设为true
        con.setDoOutput(true);
        //禁止HttpURLConnection使用缓存
        con.setUseCaches(false);
        //使用POST请求，必须大写
        con.setRequestMethod("POST");
        //以下三行设置http请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        //在模拟web页面向服务器端上传文件时，每个文件的开头需要有一个分界符，
        //分界符需要在http请求头中指定。boundary是任意一个字符串，一般为******
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + boundary);

        DataOutputStream ds = new DataOutputStream(con.getOutputStream());

        ds.writeBytes(twoHyphens + boundary + end);
        //上传文件相关信息，这些信息包括请求参数名，上传文件名，文件类型，但并不限于此
        ds.writeBytes("Content-Disposition: form-data; "
                + "name=\"file1\";filename=\"" + newName + "\"" + end);
        ds.writeBytes(end);

        //获得文件的输入流，通过流传输文件。在这里我重写了FileInputStream，为了监听上传进度
        CustomFileInputStream fStream = new CustomFileInputStream(filePath);
        fStream.setOnUploadListener(listener);
        /* 设置每次写入1024bytes */
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length = -1;
        // 从文件读取数据至缓冲区
        while ((length = fStream.read(buffer)) != -1) {
            //将资料写入DataOutputStream中
            ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

        fStream.close();
        ds.flush();

        //上传完成以后获取服务器的反馈
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }

        ds.close();
        return b.toString();
    }
	public static String HttpClientUploadFile(String urlPath, String filePath,String filename,
	                              OnUploadListener listener) throws Exception {

		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";

		URL url = new URL(urlPath);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		//下载需要将setDoInput方法的参数值设为true
		//con.setDoInput(true);
		//上传需要将setDoOutput方法的参数值设为true
		con.setDoOutput(true);
		//禁止HttpURLConnection使用缓存
		con.setUseCaches(false);
		//使用POST请求，必须大写
		con.setRequestMethod("POST");
		//以下三行设置http请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//在模拟web页面向服务器端上传文件时，每个文件的开头需要有一个分界符，
		//分界符需要在http请求头中指定。boundary是任意一个字符串，一般为******
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);

		DataOutputStream ds = new DataOutputStream(con.getOutputStream());

		ds.writeBytes(twoHyphens + boundary + end);
		//上传文件相关信息，这些信息包括请求参数名，上传文件名，文件类型，但并不限于此
		ds.writeBytes("Content-Disposition: form-data; "
				+ "name=\"file1\";filename=\"" +filename + "\"" + end);
		ds.writeBytes(end);

		//获得文件的输入流，通过流传输文件。在这里我重写了FileInputStream，为了监听上传进度
		CustomFileInputStream fStream = new CustomFileInputStream(filePath);
		fStream.setOnUploadListener(listener);
        /* 设置每次写入1024bytes */
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int length = -1;
		// 从文件读取数据至缓冲区
		while ((length = fStream.read(buffer)) != -1) {
			//将资料写入DataOutputStream中
			ds.write(buffer, 0, length);
		}
		ds.writeBytes(end);
		ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

		fStream.close();
		ds.flush();

		//上传完成以后获取服务器的反馈
		/**
		 * 获取响应码  200=成功
		 * 当响应成功，获取响应的流
		 */
		int res = con.getResponseCode();

		if(res==200)
		{
			listener.onUpload(200);
			return "成功";
		}else{
			listener.onUpload(100);
		}
		InputStream is = con.getInputStream();
		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}

		ds.close();
		return b.toString();
	}
}
