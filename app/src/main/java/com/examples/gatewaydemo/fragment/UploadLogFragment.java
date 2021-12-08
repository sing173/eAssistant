package com.examples.gatewaydemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.activity.FirstActivity;
import com.examples.gatewaydemo.base.BaseFragment;
import com.examples.gatewaydemo.httputils.HttpUploadUtil;
import com.examples.gatewaydemo.httputils.OnUploadListener;
import com.examples.gatewaydemo.util.ConstantValue;
import com.examples.gatewaydemo.util.DateFormatUtil;
import com.examples.gatewaydemo.util.LogUtils;
import com.examples.gatewaydemo.util.ToastUtil;
import com.examples.gatewaydemo.util.Tool;
import com.examples.gatewaydemo.util.UrlUtil;
import com.examples.gatewaydemo.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UploadLogFragment extends BaseFragment implements OnClickListener{
	private View view;
	private Button zipTransLogBt,uploadCrashLogBt,uploadTransLogBt;
	private Context context;
	private String Path="";

	public UploadLogFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_upload_log, null);
		initView();
		Listener();
		return view;
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		zipTransLogBt=(Button)view.findViewById(R.id.zipTransLogBt);
		uploadCrashLogBt=(Button)view.findViewById(R.id.uploadCrashLogBt);
		uploadTransLogBt=(Button)view.findViewById(R.id.uploadTransLogBt);
		//删除七天外日志文件
		Tool.deleteFileByDate(ConstantValue.LogTransdir);
	}

	@Override
	public void Listener() {
		// TODO Auto-generated method stub
		zipTransLogBt.setOnClickListener(this);
		uploadTransLogBt.setOnClickListener(this);
		uploadCrashLogBt.setOnClickListener(this);
	}

	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//progressBar.setVisibility(View.GONE);
			if(msg.what==ConstantValue.UploadFileSuccess){
				ToastUtil.showLongToast(context, "上传异常日志成功！");
				//zipTransLog();
			}else if(msg.what==ConstantValue.UploadFilefailed){
				ToastUtil.showLongToast(context, "上传异常日志失败！");
			}else if(msg.what==ConstantValue.ZipFolderSuccess){
				ToastUtil.showLongToast(context,"交易日志压缩成功！");
				//uploadTransLog();
			}else if(msg.what==ConstantValue.ZipFolderFail){
				ToastUtil.showLongToast(context,"交易日志压缩失败！");
			}else if(msg.what==ConstantValue.UploadTransLogFileSuccess){
				ToastUtil.showLongToast(context, "上传交易日志成功！");
			}else if(msg.what==ConstantValue.UploadTransLogFilefailed){
				ToastUtil.showLongToast(context, "上传交易日志失败！");
			}
		}
	};

	@Override
	public void onClick(View view) {
		int id=view.getId();
		switch (id){
			case R.id.zipTransLogBt:
				if(Tool.isFastDouleClick()) {
					zipTransLog();
				}
				break;
			case R.id.uploadCrashLogBt:
				if(Tool.isFastDouleClick()) {
					uploadCrashLogFile();
				}
				break;
			case R.id.uploadTransLogBt:
				if(Tool.isFastDouleClick()) {
					uploadTransLog();
				}
				break;
		}
	}

	/**
	 * Create by hutian 2018-07-12
	 * 上传 最近一个交易日的异常日志
	 */
	private void uploadCrashLogFile() {
		String errorPath = ConstantValue.FkezsCrashLogdir;
		final String actionUrl= UrlUtil.getServiceIp()+"FileUploadServlet.do?method=fkeLogFile";
		//final String actionUrl= "http://192.168.3.36:9090/trunk/FileUploadServlet.do?method=fkeLogFile";
		File crashDir=new File(errorPath);
		if(!crashDir.exists()){
			ToastUtil.showLongToast(context,"异常日志文件目录不存在或被删除");
			return;
		}
		//实现上传最接近当前日志的文件算法逻辑 (最多保存七个文件 )
		ArrayList<String> arrayList= DateFormatUtil.getWeekData();
		if(arrayList!=null&&arrayList.size()>0){
			for(int i=0;i<arrayList.size();i++){
				String filename=arrayList.get(i);
				String file="crash-"+filename+".log";
				File crashFile=new File(errorPath,file);
				if(crashFile.exists()){//有最近期的日志
					Path=errorPath+ File.separator +file;//绝对文件路径
					break;
				}else{
					//ToastUtil.showLongToast(context,"无近期异常日志上传！开始压缩交易日志");
					//uploadTransLog();
					//zipTransLog();
				}
			}
		}
		//final String Path=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash/test.txt";
		//String actionUrl="http://192.168.2.111:7001/FileUploadServlet.do?method=fkeLogFile";
		final File filePath=new File(Path);
		if(!filePath.exists()){
			LogUtils.d("上传文件不存在");
			return;
		}
		//HttpUploadUtil.HttpUploadRequest(FirstActivity.this,actionUrl,Path);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// UploadUtil.uploadFile(filePath,actionUrl);
					HttpUploadUtil.HttpClientUploadFile(actionUrl, Path,"fkezslog.txt", new OnUploadListener() {
						@Override
						public void onUpload(double process) {
							Message msg=new Message();
							if(process==200){
								msg.what=ConstantValue.UploadFileSuccess;
							}else if(process==100){
								msg.what=ConstantValue.UploadFilefailed;
							}
							mHandler.sendMessage(msg);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void uploadTransLog(){
		final String transZipPath = ConstantValue.LogTransdir+"/transLog-"+DateFormatUtil.getCurrentDate()+".zip";
		final String actionUrl= UrlUtil.getServiceIp()+"FileUploadServlet.do?method=fkeLogFile";
		final String filename="transLog-"+DateFormatUtil.getCurrentDate()+".zip";
		//final String actionUrl= "http://192.168.3.36:9090/trunk/FileUploadServlet.do?method=fkeLogFile";
		File crashDir=new File(transZipPath);
		if(!crashDir.exists()){
			LogUtils.d("上传文件不存在");
			ToastUtil.showLongToast(context,"未发现上传文件，请先压缩！");
			return;
		}
		//HttpUploadUtil.HttpUploadRequest(FirstActivity.this,actionUrl,Path);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// UploadUtil.uploadFile(filePath,actionUrl);
					HttpUploadUtil.HttpClientUploadFile(actionUrl,transZipPath,filename, new OnUploadListener() {
						@Override
						public void onUpload(double process) {
							Message msg=new Message();
							if(process==200){
								msg.what=ConstantValue.UploadTransLogFileSuccess;
							}else if(process==100){
								msg.what=ConstantValue.UploadTransLogFilefailed;
							}
							mHandler.sendMessage(msg);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void zipTransLog(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int flag= ZipUtil.zip(context,ConstantValue.LogTransdir,ConstantValue.LogTransdir+"/transLog-"+DateFormatUtil.getCurrentDate()+".zip");
					if(flag==1){
						Message msg=new Message();
						msg.what=ConstantValue.ZipFolderSuccess;
						mHandler.sendMessage(msg);
					}else{
						Message msg=new Message();
						msg.what=ConstantValue.ZipFolderFail;
						mHandler.sendMessage(msg);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
