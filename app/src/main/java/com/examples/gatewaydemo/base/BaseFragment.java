package com.examples.gatewaydemo.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
	public abstract void initView();
	public abstract void Listener();
}
