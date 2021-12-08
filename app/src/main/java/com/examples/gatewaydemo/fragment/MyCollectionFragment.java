package com.examples.gatewaydemo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseFragment;
import com.examples.gatewaydemo.model.AppInfo;
import com.examples.gatewaydemo.model.User;
import com.examples.gatewaydemo.util.PreferencesUtils;

public class MyCollectionFragment extends BaseFragment implements OnClickListener{
	private View view;
	private ListView listView;
	private List<AppInfo> appInfoList;
	private CollectionAdapter adapter;
	private AppInfo info,info2,info3,info4;
	private PackageInfo packageInfo;
	private PreferencesUtils pre;
	private String userId;
	private User user;
	
	public MyCollectionFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public MyCollectionFragment(String userId, User user){
		this.userId=userId;
		this.user=user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_collection, null);
		initView();
		Listener();
		return view;
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		listView=(ListView) view.findViewById(R.id.my_collection_listview);
		appInfoList=new ArrayList<AppInfo>();
		pre=new PreferencesUtils(getActivity(), "collectionInfo");
		String collecString=pre.getString("collecString", "");
		dataInitView();
		adapter=new CollectionAdapter(appInfoList, getActivity());
		listView.setAdapter(adapter);
	}
	
	private void dataInitView(){
		info=new AppInfo();
		info2=new AppInfo();
		info3=new AppInfo();
		info4=new AppInfo();
		setData();
		appInfoList.add(info);
		appInfoList.add(info2);
		appInfoList.add(info3);
		appInfoList.add(info4);
	}
	
	private void setData(){
		info.appName="测试1";
		info.appRemark="测试1";
		info.appVersion="1.2.0";
		info.updateTime="2016-12-6";
		info.packageName="com.examples.gatewaytest1";
		info.activityName="com.examples.gatewaytest1.MainActivity";
		info.iconFlag="1";
		
		info2.appName="测试2";
		info2.appRemark="测试2";
		info2.appVersion="1.2.1";
		info2.updateTime="2016-11-6";
		info2.packageName="com.examples.gatewaytest2";
		info2.activityName="com.examples.gatewaytest2.MainActivity";
		info2.iconFlag="2";
		
		info3.appName="测试3";
		info3.appRemark="测试3";
		info3.appVersion="1.2.1";
		info3.updateTime="2016-11-6";
		info3.packageName="com.examples.gatewaytest3";
		info3.activityName="com.examples.gatewaytest3.MainActivity";
		info3.iconFlag="3";
		
		info4.appName="测试4";
		info4.appRemark="测试4";
		info4.appVersion="1.2.1";
		info4.updateTime="2016-11-6";
		info4.packageName="com.examples.gatewaytest4";
		info4.activityName="com.examples.gatewaytest4.MainActivity";
		info4.iconFlag="4";
	}

	@Override
	public void Listener() {
		// TODO Auto-generated method stub
		
	}
	
	public class CollectionAdapter extends BaseAdapter{
		private List<AppInfo> appInfoList;
		private Context context;
		
		public CollectionAdapter(List<AppInfo> appInfoList,Context context){
			this.appInfoList=appInfoList;
			this.context=context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(null!=appInfoList){
				return appInfoList.size();
			}else{
			return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return appInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder=null;
			if(null==convertView){
				holder=new Holder();
				convertView=LayoutInflater.from(context).inflate(R.layout.item_app_info_listview, null);
				holder.appName=(TextView) convertView.findViewById(R.id.appName);
				holder.appRemark=(TextView) convertView.findViewById(R.id.appRemark);
				holder.appVersion=(TextView) convertView.findViewById(R.id.appVersion);
				holder.updateTime=(TextView) convertView.findViewById(R.id.appUpdateTime);
				holder.addApp=(Button) convertView.findViewById(R.id.add_app);
				holder.deleteApp=(Button) convertView.findViewById(R.id.delete_app);
				holder.updateApp=(Button) convertView.findViewById(R.id.update_app);
				holder.appSrc=(ImageView) convertView.findViewById(R.id.appSrc);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			
			if(null!=appInfoList.get(position).appName&&!"".equals(appInfoList.get(position).appName)){
				holder.appName.setText(appInfoList.get(position).appName);
			}
			if(null!=appInfoList.get(position).appRemark&&!"".equals(appInfoList.get(position).appRemark)){
				holder.appRemark.setText(appInfoList.get(position).appRemark);
			}
			if(null!=appInfoList.get(position).appVersion&&!"".equals(appInfoList.get(position).appVersion)){
				holder.appVersion.setText(appInfoList.get(position).appVersion);
			}
			if(null!=appInfoList.get(position).updateTime&&!"".equals(appInfoList.get(position).updateTime)){
				holder.updateTime.setText(appInfoList.get(position).updateTime);
			}
			if(null!=appInfoList.get(position).iconFlag&&!"".equals(appInfoList.get(position).iconFlag)){
				if("1".equals(appInfoList.get(position).iconFlag)){
					holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon01));
				}else if("2".equals(appInfoList.get(position).iconFlag)){
					holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon02));
				}else if("3".equals(appInfoList.get(position).iconFlag)){
					holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon03));
				}else if("4".equals(appInfoList.get(position).iconFlag)){
					holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon04));
				}
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {  
					     packageInfo = context.getPackageManager().getPackageInfo(  
					     appInfoList.get(position).packageName, PackageManager.GET_ACTIVITIES);  
					 } catch (PackageManager.NameNotFoundException e) {  
					     packageInfo = null;  
					     e.printStackTrace();  
					  }  
					   if(packageInfo ==null){  
					            Toast.makeText(getActivity(), "应用未安装，请安装之后再进行此操作！", 3000).show();
					        }else{  
					            moveTo(position,userId); 
					       }
					
				}
			});
			
			holder.deleteApp.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					    Uri uri = Uri.parse("package:" + appInfoList.get(position).packageName);  
					    Intent intent = new Intent(Intent.ACTION_DELETE, uri);  
					    context.startActivity(intent);    
				}
			});
			
			holder.updateApp.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "已是最新版本！", 3000).show();
				}
			});
			
			return convertView;
		}
		
		 class Holder{
			TextView appName;
			TextView appRemark;
			TextView appVersion;
			TextView updateTime;
			Button addApp;
			Button deleteApp;
			Button updateApp;
			ImageView appSrc;
		 }
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private void moveTo(int position,String userId){
		ComponentName component=new ComponentName(appInfoList.get(position).packageName,				appInfoList.get(position).activityName);
		Intent intent=new Intent();
		Bundle bundle=new Bundle();
		bundle.putString("userId", userId);
		Log.i("---","---"+userId);
		intent.putExtras(bundle);
		intent.setComponent(component);
		startActivity(intent);
	}

}
