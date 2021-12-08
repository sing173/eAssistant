package com.examples.gatewaydemo.util;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.adapter.CommonTypeAdapter;

/**
 * Created by Administrator on 2018/3/4.
 */

public class ShowCommonWindow {
	private static final String TAG = "ShowCommonTypeWindow";
	  private static PopupWindow certpop;
	  private static PopupWindow branchPop;
	  private static PopupWindow managerPop;
	  private static PopupWindow productPop;
	  private static CommonTypeAdapter adapter = null;
	  
	  public static void showCommonTypeWindow(Context context, String[] cert, final TextView text,int screenWidth,int screenHeight) {
	    LayoutInflater inflate = LayoutInflater.from(context);
	    View view = inflate.inflate(R.layout.pop_common_type, null);
	    ListView lv_commonPop = (ListView) view.findViewById(R.id.common_type_lv);

	    // 显示pop弹框
	    if (certpop == null) {
	      certpop = new PopupWindow(context);
	      certpop.setWidth(screenWidth/5);
	      certpop.setHeight(screenHeight/5);
	      certpop.setContentView(view);
	      certpop.setBackgroundDrawable(new ColorDrawable(0x00000000));
	      // pop.showAsDropDown(text,-screenWidth / 3 + Tool.dip2px(context,500),
	      // 20);
	      // pop.showAtLocation(view, Gravity.CENTER, -200, 10);
	      certpop.setFocusable(true);
	    }
	    certpop.showAsDropDown(text, 0, 0);
	    if (adapter == null) {
	      adapter = new CommonTypeAdapter(context, cert, text, certpop);
	      lv_commonPop.setAdapter(adapter);
	    } else {
	      adapter.list = cert;
	      adapter.text = text;
	      adapter.notifyDataSetChanged();
	    }
	  }
}
