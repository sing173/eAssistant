package com.examples.gatewaydemo.util;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.model.BlueDeviceInfo;

/**
 * Created by Administrator on 2018/3/4.
 */

public class ShowCommonTypeWindow {
    private static PopupWindow certpop;
    private static CommonTypeAdapter adapter;
    private static int DEVICE_GUOGUANG_ID = 1;
    private static int DEVICE_SHENSI_ID = 2;
    private static int DEVICE_WOQI_ID = 3;
    public static void showCommonTypeWindow(Context context, List<BlueDeviceInfo> cert, final TextView text, int screenWidth, int screenHeight) {
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
    
    private static class CommonTypeAdapter extends BaseAdapter{
        private static final String TAG = "CommonTypeAdapter";
        //证件信息类型适配器
        private Context context;
        //之后需要不同地方调用并更改，所以需要使用public
        public List<BlueDeviceInfo> list;
        public TextView text;
        private PopupWindow certpop;

        public CommonTypeAdapter(Context context, List<BlueDeviceInfo> list, TextView text, PopupWindow certpop) {
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
                return list.size();
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
            return list.get(position);
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
            if(null!=list.get(position)){
                holder.tvCertName.setText(list.get(position).getBlueDeviceName());
            }
            //点击pop框消失并把选中的信息显示在对应textview中
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    text.setText(list.get(position).getBlueDeviceName());
                    PreferencesUtils pre=new PreferencesUtils(context,"BlueTooth");
                    Log.i("---------", "---2---macAddress-----"+list.get(position).getBlueDeviceAddress());
                    if(list.get(position).equals("请选择蓝牙设备")){

                    }else if(list.get(position).getBlueDeviceName().contains("已选握奇设备")){
                        pre.putInt("deviceId",DEVICE_WOQI_ID);
                        pre.putString("whetherConnected","no");
                        pre.putString("deviceName",list.get(position).getBlueDeviceName());
                        pre.putString("macAddress", list.get(position).getBlueDeviceAddress());
                    }else if(list.get(position).getBlueDeviceName().contains("已选神思设备")){
                        pre.putInt("deviceId",DEVICE_SHENSI_ID);
                        pre.putString("whetherConnected","no");
                        pre.putString("deviceName",list.get(position).getBlueDeviceName());
                        pre.putString("macAddress", list.get(position).getBlueDeviceAddress());
                    }else if(list.get(position).getBlueDeviceName().contains("已选国光设备")){
                        pre.putInt("deviceId",DEVICE_GUOGUANG_ID);
                        pre.putString("whetherConnected","no");
                        pre.putString("deviceName",list.get(position).getBlueDeviceName());
                        pre.putString("macAddress", list.get(position).getBlueDeviceAddress());
                    }
                    certpop.dismiss();
                }
            });

            return convertView;
        }

        class ViewHolder {
            private TextView tvCertName;
        }
    }
}
