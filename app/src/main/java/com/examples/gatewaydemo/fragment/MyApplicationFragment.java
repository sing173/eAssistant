package com.examples.gatewaydemo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseFragment;
import com.examples.gatewaydemo.model.AppInfo;
import com.examples.gatewaydemo.model.RelationInfo;
import com.examples.gatewaydemo.model.User;
import com.examples.gatewaydemo.util.ConstantValue;
import com.examples.gatewaydemo.util.DateFormatUtil;
import com.examples.gatewaydemo.util.ToastUtil;

public class MyApplicationFragment extends BaseFragment {
    private View                 view;
    private ListView             listView;
    private List<AppInfo>        appInfoList;
    private MyApplicationAdapter adapter;
    private PackageManager       pageManager;
    private List<PackageInfo>    packages;
    private PackageInfo          packageInfo;
    private String               userId;
    private Context              context;
    private BluetoothAdapter     mBlueToothAdapter;
    private User user;


    public MyApplicationFragment() {
        // TODO Auto-generated constructor stub
    }

    public MyApplicationFragment(String userId, Context context, BluetoothAdapter mBlueToothAdapter,User user) {
        this.userId = userId;
        this.context = context;
        this.mBlueToothAdapter = mBlueToothAdapter;
        this.user=user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_application, null);
        initView();
        Listener();
        return view;
    }

    @Override
    public void initView() {
        listView = (ListView) view.findViewById(R.id.my_application_listview);
        appInfoList = new ArrayList<AppInfo>();
        getApp();
        adapter = new MyApplicationAdapter(appInfoList, getActivity(), userId);
        listView.setAdapter(adapter);
    }

    private void getApp() {
        // TODO Auto-generated method stub
        pageManager = getActivity().getPackageManager();
        packages = pageManager.getInstalledPackages(0);
        // 获得包名
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            //if(packageInfo.packageName.contains("gatewaytest")){
            if (packageInfo.packageName.contains("fkezs")) {
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appRemark = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appVersion = packageInfo.versionCode + "";
                info.updateTime = DateFormatUtil.getDateToString(packageInfo.lastUpdateTime);
                info.packageName = packageInfo.packageName;
                info.activityName = "com.dysen.opencard" + ".LoginActivity";
                info.iconFlag = "1";
                appInfoList.add(info);
            } else if (packageInfo.packageName.contains("financialmanager")) {//com.pactera.financialmanager 客户经理
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appRemark = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appVersion = packageInfo.versionCode + "";
                info.updateTime = DateFormatUtil.getDateToString(packageInfo.lastUpdateTime);
                info.packageName = packageInfo.packageName; //.ui.credit.CreditCardApplyListActivity .ui.login.NewLoginActivity
                info.activityName = "com.pactera.financialmanager" + ".ui.credit.CreditCardApplyListActivity";
                info.iconFlag = "2";
                appInfoList.add(info);
            } else if(packageInfo.packageName.contains("creditcardentry")){//信用卡进件
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appRemark = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appVersion = packageInfo.versionCode + "";
                info.updateTime = DateFormatUtil.getDateToString(packageInfo.lastUpdateTime);
                info.packageName = packageInfo.packageName;
                info.activityName = "com.pactera.hbnx.creditcardentry.activity.CreditListActivity";
                info.iconFlag = "1";
                appInfoList.add(info);
            }
        }
    }

    @Override
    public void Listener() {
        // TODO Auto-generated method stub

    }

    public class MyApplicationAdapter extends BaseAdapter {
        private List<AppInfo> appInfoList;
        private Context       context;
        private String        userId;

        public MyApplicationAdapter(List<AppInfo> appInfoList, Context context, String userId) {
            this.appInfoList = appInfoList;
            this.context = context;
            this.userId = userId;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (null != appInfoList) {
                return appInfoList.size();
            } else {
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
            Holder holder = null;
            if (null == convertView) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_app_info_listview, null);
                holder.appName = (TextView) convertView.findViewById(R.id.appName);
                holder.appRemark = (TextView) convertView.findViewById(R.id.appRemark);
                holder.appVersion = (TextView) convertView.findViewById(R.id.appVersion);
                holder.updateTime = (TextView) convertView.findViewById(R.id.appUpdateTime);
                holder.addApp = (Button) convertView.findViewById(R.id.add_app);
                holder.deleteApp = (Button) convertView.findViewById(R.id.delete_app);
                holder.updateApp = (Button) convertView.findViewById(R.id.update_app);
                holder.appSrc = (ImageView) convertView.findViewById(R.id.appSrc);
                holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            if (null != appInfoList.get(position).appName && !"".equals(appInfoList.get(position).appName)) {
                holder.appName.setText(appInfoList.get(position).appName);
            }
            if (null != appInfoList.get(position).appRemark && !"".equals(appInfoList.get(position).appRemark)) {
                holder.appRemark.setText(appInfoList.get(position).appRemark);
            }
            if (null != appInfoList.get(position).appVersion && !"".equals(appInfoList.get(position).appVersion)) {
                holder.appVersion.setText(appInfoList.get(position).appVersion);
            }
            if (null != appInfoList.get(position).updateTime && !"".equals(appInfoList.get(position).updateTime)) {
                holder.updateTime.setText(appInfoList.get(position).updateTime);
            }
            if (null != appInfoList.get(position).iconFlag && !"".equals(appInfoList.get(position).iconFlag)) {
                if ("1".equals(appInfoList.get(position).iconFlag)) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon01));
                } else if ("2".equals(appInfoList.get(position).iconFlag)) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.ic_customer_manager));
                } else if ("3".equals(appInfoList.get(position).iconFlag)) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon03));
                } else if ("4".equals(appInfoList.get(position).iconFlag)) {
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
                    if (packageInfo == null) {
                        Toast.makeText(getActivity(), "应用未安装，请安装之后再进行此操作！", 3000).show();
                    } else {
                        moveTo(position, userId);
                        if (mBlueToothAdapter != null) {
                            if (mBlueToothAdapter.enable()) {
                                mBlueToothAdapter.disable();//关闭设备连接
                            }
                        }
                    }

                }
            });

            holder.deleteApp.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Uri    uri    = Uri.parse("package:" + appInfoList.get(position).packageName);
                    Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                    //context.startActivity(intent);
                    startActivityForResult(intent, 0);
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

        class Holder {
            TextView    appName;
            TextView    appRemark;
            TextView    appVersion;
            TextView    updateTime;
            Button      addApp;
            Button      deleteApp;
            Button      updateApp;
            ImageView   appSrc;
            ProgressBar progressBar;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 0) {
            initView();
			/*getApp();
			adapter=new MyApplicationAdapter(appInfoList, getActivity(),userId);
			listView.setAdapter(adapter);*/
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void moveTo(int position, String userId) {
        ComponentName component = new ComponentName(appInfoList.get(position).packageName,                appInfoList.get(position).activityName);
        //Intent intent=context.getPackageManager().getLaunchIntentForPackage(appInfoList.get(position).packageName);
        String type   = appInfoList.get(position).iconFlag;
        Intent intent = new Intent();
        //Bundle bundle=new Bundle();
        //bundle.putString("userId", userId);
        Log.i("---", "---" + userId);

        if (intent == null) {
            ToastUtil.showLongToast(context, "未安装此app");
        } else {
            intent.setComponent(component);
            //update by hutain 2918-07-13
            List<RelationInfo> relationInfos = ConstantValue.relationInfos;
            if ("1".equals(type) && relationInfos.size() > 0) {//福卡e助手需要的参数
                for (RelationInfo relationInfo : ConstantValue.relationInfos) {
                    if (relationInfo.getSystemName().equals("com.dysen.opencard")) {
                        intent.putExtra("tellerId", relationInfo.getTellId());
                        intent.putExtra("orgId", relationInfo.getBrCode());
                    }
                }

            } else if ("2".equals(type) && relationInfos.size() > 0) {//信用卡进件，客户经理
                for (RelationInfo relationInfo : ConstantValue.relationInfos) {
                    if (relationInfo.getSystemName().equals("com.pactera.financialmanager")) {
                    }
                }
            } else if("1".equals(type)&&appInfoList.get(position).packageName.contains("creditcardentry")){
                intent.putExtra("brCode",user.getBrCode());
                intent.putExtra("certCode",user.getCERTCODE());
            }
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
