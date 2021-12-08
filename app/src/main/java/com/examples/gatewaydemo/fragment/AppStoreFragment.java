package com.examples.gatewaydemo.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseFragment;
import com.examples.gatewaydemo.model.User;

public class AppStoreFragment extends BaseFragment implements OnClickListener{
	private View view;
	private Button myCollectionBt;
	private Button myApplicationBt;
	private Button allApplicationBt;
	private Fragment myCollectionFragment;
	private Fragment myApplicationFragment;
	private Fragment allApplicationFragment;
	private FragmentManager manager;
	private FragmentTransaction tx;
	private String userId;
	private BluetoothAdapter mBlueToothAdapter;
	private User user;
	
	public AppStoreFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public AppStoreFragment (String userId, BluetoothAdapter mBlueToothAdapter, User user){
		this.userId=userId;
		this.mBlueToothAdapter=mBlueToothAdapter;
		this.user=user;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_app_stpre, null);
		initView();
		Listener();
		return view;
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		myCollectionBt=(Button) view.findViewById(R.id.my_collection);
		myApplicationBt=(Button) view.findViewById(R.id.my_application);
		allApplicationBt=(Button) view.findViewById(R.id.all_application);
		myCollectionFragment=new MyCollectionFragment(userId,user);
		myApplicationFragment=new MyApplicationFragment(userId,getActivity(),mBlueToothAdapter,user);
		allApplicationFragment=new AllApplicationFragment(userId,mBlueToothAdapter,user);
		
		/*myCollectionBt.setBackgroundColor(getResources().getColor(R.color.red));
		myCollectionBt.setTextColor(getResources().getColor(R.color.white));
		myApplicationBt.setTextColor(getResources().getColor(R.color.black));
		allApplicationBt.setTextColor(getResources().getColor(R.color.black));*/
		manager=getFragmentManager();
		tx=manager.beginTransaction();
		tx.replace(R.id.content_listview, allApplicationFragment);
		tx.commit();
		
	}

	@Override
	public void Listener() {
		// TODO Auto-generated method stub
		myCollectionBt.setOnClickListener(this);
		myApplicationBt.setOnClickListener(this);
		allApplicationBt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		tx=manager.beginTransaction();
		// TODO Auto-generated method stub
		int Id=v.getId();
		switch (Id) {
		case R.id.my_collection:
			/*myCollectionBt.setBackgroundColor(getResources().getColor(R.color.red));
			myApplicationBt.setBackgroundColor(getResources().getColor(R.color.light_gray));
			allApplicationBt.setBackgroundColor(getResources().getColor(R.color.light_gray));
			myCollectionBt.setTextColor(getResources().getColor(R.color.white));
			myApplicationBt.setTextColor(getResources().getColor(R.color.black));
			allApplicationBt.setTextColor(getResources().getColor(R.color.black));*/
			tx.replace(R.id.content_listview, myCollectionFragment);
			tx.commit();
			break;
		case R.id.my_application:
			/*myCollectionBt.setBackgroundColor(getResources().getColor(R.color.light_gray));
			myApplicationBt.setBackgroundColor(getResources().getColor(R.color.red));
			allApplicationBt.setBackgroundColor(getResources().getColor(R.color.light_gray));
			myApplicationBt.setTextColor(getResources().getColor(R.color.white));
			myCollectionBt.setTextColor(getResources().getColor(R.color.black));
			allApplicationBt.setTextColor(getResources().getColor(R.color.black));*/
			tx.replace(R.id.content_listview, myApplicationFragment);
			tx.commit();
			break;
		case R.id.all_application:
			/*myCollectionBt.setBackgroundColor(getResources().getColor(R.color.light_gray));
			myApplicationBt.setBackgroundColor(getResources().getColor(R.color.light_gray));
			allApplicationBt.setBackgroundColor(getResources().getColor(R.color.red));
			allApplicationBt.setTextColor(getResources().getColor(R.color.white));
			myApplicationBt.setTextColor(getResources().getColor(R.color.black));
			myCollectionBt.setTextColor(getResources().getColor(R.color.black));*/
			tx.replace(R.id.content_listview,allApplicationFragment);
			tx.commit();
			break;

		default:
			break;
		}
	}

}
