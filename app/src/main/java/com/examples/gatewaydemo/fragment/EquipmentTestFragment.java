package com.examples.gatewaydemo.fragment;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseFragment;
import com.examples.gatewaydemo.util.LogUtils;
import com.examples.gatewaydemo.util.PreferencesUtils;
import com.examples.gatewaydemo.util.Tool;
import com.examples.outputjar.BlueUtils;

public class EquipmentTestFragment extends BaseFragment implements OnClickListener{

	private View view;
	private Button blueConnectBt,idCardBt,icCardBt,magCardBt,passwordBt,fingerBt;
	private LinearLayout lrContent1,lrContent2,lrContent3,lrContent4,lrContent5,lrContent6,lrContent7,lrContent8,lrContent9;
	private ImageView headPhoto;
	private TextView describeTx1,describeTx2,describeTx3,describeTx4,describeTx5,describeTx6,describeTx7,describeTx8,describeTx9;
	private TextView contentTx1,contentTx2,contentTx3,contentTx4,contentTx5,contentTx6,contentTx7,contentTx8,contentTx9;
	private HashMap<String, String> hashMap=new HashMap<String, String>();
	private HashMap<String, Bitmap> bitMap=new HashMap<String, Bitmap>();
	private int deviceId;
	private PreferencesUtils pre;
	private String macAddress;
	
	public EquipmentTestFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public EquipmentTestFragment(int deviceId) {
		// TODO Auto-generated constructor stub
		this.deviceId=deviceId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_equipment_test, null);
		initView();
		Listener();
		return view;
	}
	
	private Handler handler=new Handler(){
	    public void handleMessage(Message msg) {
	      super.handleMessage(msg);
	      if(msg.what==0){
	        Toast.makeText(getActivity(), "蓝牙连接成功", Toast.LENGTH_LONG).show();
	        pre.putInt("deviceId",deviceId);
            pre.putString("whetherConnected","yes");
	      }else if(msg.what==1){
	    	Toast.makeText(getActivity(), "蓝牙连接失败", Toast.LENGTH_LONG).show();
	      }else if(msg.what==2){
	    	Toast.makeText(getActivity(), "证件信息获取成功", Toast.LENGTH_LONG).show(); 
	    	//headPhoto.setImageBitmap(bitMap.get("icon"));
		      contentTx1.setText(hashMap.get("name").trim());
		      contentTx2.setText(hashMap.get("sex"));
		      contentTx3.setText(hashMap.get("nation"));
		      contentTx4.setText(hashMap.get("birthday"));
		      contentTx5.setText(hashMap.get("idAddr").trim());
		      contentTx6.setText(hashMap.get("idNo"));
		      contentTx7.setText(hashMap.get("idOrg").trim());
		      contentTx8.setText(hashMap.get("beginDate"));
		      contentTx9.setText(hashMap.get("endDate"));
	      }else if(msg.what==3){
	    	Toast.makeText(getActivity(), "证件信息获取失败", Toast.LENGTH_LONG).show();
	      }else if(msg.what==4){
	    	Toast.makeText(getActivity(), "IC卡信息获取成功", Toast.LENGTH_LONG).show(); 
	    	contentTx1.setText(hashMap.get("cardNo"));
	      }else if(msg.what==5){
	    	Toast.makeText(getActivity(), "IC卡信息获取失败", Toast.LENGTH_LONG).show(); 
	      }else if(msg.what==6){
		    Toast.makeText(getActivity(), "磁条卡信息获取成功", Toast.LENGTH_LONG).show(); 
		    contentTx1.setText(hashMap.get("cardNo"));
		  }else if(msg.what==7){
		    Toast.makeText(getActivity(), "磁条卡信息获取失败", Toast.LENGTH_LONG).show(); 
		  }else if(msg.what==8){
			Toast.makeText(getActivity(), "密码信息获取成功", Toast.LENGTH_LONG).show(); 
			contentTx1.setText(hashMap.get("password"));
		  }else if(msg.what==9){
			Toast.makeText(getActivity(), "密码信息获取失败", Toast.LENGTH_LONG).show(); 
		  }else if(msg.what==10){
			Toast.makeText(getActivity(), "指纹信息获取成功", Toast.LENGTH_LONG).show(); 
			Toast.makeText(getActivity(), hashMap.get("fingerKey"), Toast.LENGTH_LONG).show(); 
		  }else if(msg.what==11){
			Toast.makeText(getActivity(), "指纹信息获取失败", Toast.LENGTH_LONG).show(); 
		  }
	    };
	  };
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		blueConnectBt=(Button) view.findViewById(R.id.BlueToothBt);
		idCardBt=(Button) view.findViewById(R.id.IdCardBt);
		icCardBt=(Button) view.findViewById(R.id.IcCardBt);
		magCardBt=(Button) view.findViewById(R.id.MagCardBt);
		passwordBt=(Button) view.findViewById(R.id.PasswordBt);
		fingerBt=(Button) view.findViewById(R.id.FingerBt);
		//声明显示相关控件
		lrContent1=(LinearLayout) view.findViewById(R.id.linear_content1);
		lrContent2=(LinearLayout) view.findViewById(R.id.linear_content2);
		lrContent3=(LinearLayout) view.findViewById(R.id.linear_content3);
		lrContent4=(LinearLayout) view.findViewById(R.id.linear_content4);
		lrContent5=(LinearLayout) view.findViewById(R.id.linear_content5);
		lrContent6=(LinearLayout) view.findViewById(R.id.linear_content6);
		lrContent7=(LinearLayout) view.findViewById(R.id.linear_content7);
		lrContent8=(LinearLayout) view.findViewById(R.id.linear_content8);
		lrContent9=(LinearLayout) view.findViewById(R.id.linear_content9);
		headPhoto=(ImageView) view.findViewById(R.id.img_head_photo);
		describeTx1=(TextView) view.findViewById(R.id.text_describe1);
		describeTx2=(TextView) view.findViewById(R.id.text_describe2);
		describeTx3=(TextView) view.findViewById(R.id.text_describe3);
		describeTx4=(TextView) view.findViewById(R.id.text_describe4);
		describeTx5=(TextView) view.findViewById(R.id.text_describe5);
		describeTx6=(TextView) view.findViewById(R.id.text_describe6);
		describeTx7=(TextView) view.findViewById(R.id.text_describe7);
		describeTx8=(TextView) view.findViewById(R.id.text_describe8);
		describeTx9=(TextView) view.findViewById(R.id.text_describe9);
		contentTx1=(TextView) view.findViewById(R.id.text_content1);
		contentTx2=(TextView) view.findViewById(R.id.text_content2);
		contentTx3=(TextView) view.findViewById(R.id.text_content3);
		contentTx4=(TextView) view.findViewById(R.id.text_content4);
		contentTx5=(TextView) view.findViewById(R.id.text_content5);
		contentTx6=(TextView) view.findViewById(R.id.text_content6);
		contentTx7=(TextView) view.findViewById(R.id.text_content7);
		contentTx8=(TextView) view.findViewById(R.id.text_content8);
		contentTx9=(TextView) view.findViewById(R.id.text_content9);
		
		describeTx1.setText("姓        名：");
		describeTx2.setText("性        别：");
		describeTx3.setText("民        族：");
		describeTx4.setText("出生年月：");
		describeTx5.setText("住        址：");
		describeTx6.setText("身份证号：");
		describeTx7.setText("签发机关：");
		describeTx8.setText("起始日期：");
		describeTx9.setText("结束日期：");
		pre=new PreferencesUtils(getActivity(),"BlueTooth");
        int deviceIdPre=pre.getInt("deviceId",0);
        macAddress=pre.getString("macAddress", "");
        String whetherConnected=pre.getString("whetherConnected","no");
        //if(deviceIdPre!=0&&deviceId==0){
            deviceId=deviceIdPre;
        //}
		LogUtils.info("---deviceId---"+deviceId);
		BlueUtils.ConnectDevice(deviceId, getActivity(), handler,macAddress);
	}

	@Override
	public void Listener() {
		// TODO Auto-generated method stub
		blueConnectBt.setOnClickListener(this);
		idCardBt.setOnClickListener(this);
		icCardBt.setOnClickListener(this);
		magCardBt.setOnClickListener(this);
		passwordBt.setOnClickListener(this);
		fingerBt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int Id=v.getId();
		  contentTx1.setText("");
	      contentTx2.setText("");
	      contentTx3.setText("");
	      contentTx4.setText("");
	      contentTx5.setText("");
	      contentTx6.setText("");
	      contentTx7.setText("");
	      contentTx8.setText("");
	      contentTx9.setText("");
	      pre=new PreferencesUtils(getActivity(),"BlueTooth");
	      int deviceIdPre=pre.getInt("deviceId",0);
	      String whetherConnected=pre.getString("whetherConnected","no");
	      deviceId=deviceIdPre;
	    if(Tool.isFastDouleClick()){
	    switch (Id) {
		case R.id.BlueToothBt:
			BlueUtils.ConnectDevice(deviceId, getActivity(), handler,macAddress);
			break;
		case R.id.IdCardBt:
			//headPhoto.setVisibility(View.VISIBLE);
			lrContent2.setVisibility(View.VISIBLE);
			lrContent3.setVisibility(View.VISIBLE);
			lrContent4.setVisibility(View.VISIBLE);
			lrContent5.setVisibility(View.VISIBLE);
			lrContent6.setVisibility(View.VISIBLE);
			lrContent7.setVisibility(View.VISIBLE);
			lrContent8.setVisibility(View.VISIBLE);
			lrContent9.setVisibility(View.VISIBLE);
			describeTx1.setText("姓        名：");
			describeTx2.setText("性        别：");
			describeTx3.setText("民        族：");
			describeTx4.setText("出生年月：");
			describeTx5.setText("住        址：");
			describeTx6.setText("身份证号：");
			describeTx7.setText("签发机关：");
			describeTx8.setText("起始日期：");
			describeTx9.setText("结束日期：");
			BlueUtils.IdCardInfoGet(deviceId, getActivity(), handler, hashMap, bitMap);
			break;
		case R.id.IcCardBt:
			headPhoto.setVisibility(View.GONE);
			describeTx1.setText("银行卡号");
			contentTx1.setText("");
			lrContent2.setVisibility(View.GONE);
			lrContent3.setVisibility(View.GONE);
			lrContent4.setVisibility(View.GONE);
			lrContent5.setVisibility(View.GONE);
			lrContent6.setVisibility(View.GONE);
			lrContent7.setVisibility(View.GONE);
			lrContent8.setVisibility(View.GONE);
			lrContent9.setVisibility(View.GONE);
			BlueUtils.IcCardInfoGet(deviceId, getActivity(), handler, hashMap);
			break;
		case R.id.MagCardBt:
			headPhoto.setVisibility(View.GONE);
			describeTx1.setText("银行卡号");
			contentTx1.setText("");
			lrContent2.setVisibility(View.GONE);
			lrContent3.setVisibility(View.GONE);
			lrContent4.setVisibility(View.GONE);
			lrContent5.setVisibility(View.GONE);
			lrContent6.setVisibility(View.GONE);
			lrContent7.setVisibility(View.GONE);
			lrContent8.setVisibility(View.GONE);
			lrContent9.setVisibility(View.GONE);
			BlueUtils.MagCardInfoGet(deviceId, getActivity(), handler, hashMap);
			break;
		case R.id.PasswordBt:
			headPhoto.setVisibility(View.GONE);
			describeTx1.setText("加密密码");
			contentTx1.setText("");
			lrContent2.setVisibility(View.GONE);
			lrContent3.setVisibility(View.GONE);
			lrContent4.setVisibility(View.GONE);
			lrContent5.setVisibility(View.GONE);
			lrContent6.setVisibility(View.GONE);
			lrContent7.setVisibility(View.GONE);
			lrContent8.setVisibility(View.GONE);
			lrContent9.setVisibility(View.GONE);
			BlueUtils.PasswordInfoGet(deviceId, getActivity(), handler, hashMap,"6230710101010713448");
			break;
		case R.id.FingerBt:
			headPhoto.setVisibility(View.GONE);
			describeTx1.setText("指纹信息");
			contentTx1.setText("");
			lrContent2.setVisibility(View.GONE);
			lrContent3.setVisibility(View.GONE);
			lrContent4.setVisibility(View.GONE);
			lrContent5.setVisibility(View.GONE);
			lrContent6.setVisibility(View.GONE);
			lrContent7.setVisibility(View.GONE);
			lrContent8.setVisibility(View.GONE);
			lrContent9.setVisibility(View.GONE);
			BlueUtils.FingerInfoGet(deviceId, getActivity(), handler, hashMap);
			break;

		default:
			break;
			}
	      }
	}

	

}
