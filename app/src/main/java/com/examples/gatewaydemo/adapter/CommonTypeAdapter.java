/*
 * CommonTypeAdapter.java
 * classes : cc.jnbank.mobilemarketing.adapter.CommonTypeAdapter
 * @author 陈英杰
 * V 1.0.0
 * Create at 2015年10月28日 下午7:26:55
 */
package com.examples.gatewaydemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.examples.gatewaydemo.R;

/**
 * 证件信息公用适配器
 * cc.jnbank.mobilemarketing.adapter.CommonTypeAdapter
 * @author 陈英杰 <br/>
 * create at 2015年10月28日 下午7:26:55
 */
public class CommonTypeAdapter extends BaseAdapter{
  private static final String TAG = "CommonTypeAdapter";
    //证件信息类型适配器
   private Context context;
   //之后需要不同地方调用并更改，所以需要使用public 
   public String[] list;
   public TextView text;
   private PopupWindow certpop;

   public CommonTypeAdapter(Context context, String[] list, TextView text,PopupWindow certpop) {
     this.context = context;
     this.list = list;
     this.text = text;
     this.certpop=certpop;
   }

   /*
    * (non-Javadoc)
    * 
    * @see android.widget.Adapter#getCount()
    */
   @Override
   public int getCount() {
     if (list != null) {
       return list.length;
     } else {
       return 0;
     }
   }

   /*
    * (non-Javadoc)
    * 
    * @see android.widget.Adapter#getItem(int)
    */
   @Override
   public Object getItem(int position) {
     return list[position];
   }

   /*
    * (non-Javadoc)
    * 
    * @see android.widget.Adapter#getItemId(int)
    */
   @Override
   public long getItemId(int position) {
     return position;
   }

   /*
    * (non-Javadoc)
    * 
    * @see android.widget.Adapter#getView(int, android.view.View,
    * android.view.ViewGroup)
    */
   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
     final ViewHolder holder;
     if (convertView == null) {
       holder = new ViewHolder();
       convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_pop_cert_type, null);
       holder.tvCertName = (TextView) convertView.findViewById(R.id.item_tv_pop_cert_type);
       convertView.setTag(holder);
     } else {
       holder = (ViewHolder) convertView.getTag();
     }
     //进行非空判断，如果不为空则赋值
     if(null!=list[position]){
     holder.tvCertName.setText(list[position]);
     }
     //点击pop框消失并把选中的信息显示在对应textview中
     convertView.setOnClickListener(new OnClickListener() {

       @Override
       public void onClick(View v) {
         text.setText(list[position]);
         certpop.dismiss();
       }
     });

     return convertView;
   }

   class ViewHolder {
     private TextView tvCertName;
   }
}
