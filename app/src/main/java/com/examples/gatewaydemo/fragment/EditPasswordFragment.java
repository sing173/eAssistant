package com.examples.gatewaydemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseFragment;

public class EditPasswordFragment extends BaseFragment{
	private View view;
	
	public EditPasswordFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_password, null);
		initView();
		Listener();
		return view;
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Listener() {
		// TODO Auto-generated method stub
		
	}

}
