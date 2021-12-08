package com.examples.gatewaydemo.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseActivity;
import com.examples.gatewaydemo.fragment.AppStoreFragment;
import com.examples.gatewaydemo.fragment.EquipmentTestFragment;
import com.examples.gatewaydemo.fragment.UploadLogFragment;
import com.examples.gatewaydemo.model.BlueDeviceInfo;
import com.examples.gatewaydemo.model.User;
import com.examples.gatewaydemo.service.ExitService;
import com.examples.gatewaydemo.util.PreferencesUtils;
import com.examples.gatewaydemo.util.ShowCommonTypeWindow;
import com.examples.gatewaydemo.util.ToastUtil;
import com.examples.outputjar.ClsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FirstActivity extends BaseActivity implements OnClickListener {

    private TextView tvAppStore;
    private TextView tvEquipmentTest;
    private TextView tvLogUpload;
    private TextView tvChangePassword;


    private       FragmentManager     manager;
    private       FragmentTransaction mFragmentTransaction;
    private       Fragment            appStoreFragment;
    private       Fragment            uploadLogFragment;
    private       Fragment            equipmentTestFragment;
    private       Button              closeApp;
    private       Intent              exitService;
    private       MyApplication       application;
    //蓝牙相关
    private final int                 DEVICE_GUOGUANG_ID = 1;
    private final int                 DEVICE_SHENSI_ID   = 2;
    private final int                 DEVICE_WOQI_ID     = 3;
    private       int                 deviceId;
    private       ParingReceived      pReceiver;
    private       String              tempAddress;
    private       String              tempPin;
    private       int                 searchGuoguang, searchShensi, searchWoqi;
    private int bondedGuoguang, bondedShensi, bondedWoqi;
    private int neededConGuoguang, neededConShensi, neededConWoqi;
    private TextView             userTitle;
    private TextView             brNameTitle;
    private TextView             chooseDeviceName;
    private BluetoothManager     blueToothManager;
    private BluetoothAdapter     mBlueToothAdapter;
    private String               deviceName;
    private List<BlueDeviceInfo> blueDevice = new ArrayList<BlueDeviceInfo>();
    private ProgressBar          progressBar;
    private ImageView            refreshDeviceImg;
    private String               Path       = "";
    private TextView             tv_version_num;
    private User user=new User();

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.fragment_first);
        tvAppStore = (TextView) findViewById(R.id.tv_app_store);
        tvChangePassword = (TextView) findViewById(R.id.tv_change_password);
        tvEquipmentTest = (TextView) findViewById(R.id.tv_equipment_test);
        tvLogUpload = (TextView) findViewById(R.id.tv_log_upload);
        closeApp = (Button) findViewById(R.id.close_app);
        userTitle = (TextView) findViewById(R.id.user_title);
        brNameTitle = (TextView) findViewById(R.id.brname);
        chooseDeviceName = (TextView) findViewById(R.id.choose_device_name);
        progressBar = (ProgressBar) findViewById(R.id.refresh_progressBar);
        refreshDeviceImg = (ImageView) findViewById(R.id.refresh_device_img);
        tv_version_num = (TextView) findViewById(R.id.tv_version_num);

        Intent intent = getIntent();
        exitService = new Intent(FirstActivity.this, ExitService.class);
        startService(exitService);
        application = MyApplication.getInstance();
        Bundle bundle   = intent.getExtras();
        String userId   = bundle.getString("userId");
        String userName = bundle.getString("userName");
        String brname   = bundle.getString("brName");
        String pswlater = bundle.getString("pswlater");
        user.setUserID(userId);
        user.setUsername(userName);
        user.setBrName(brname);
        user.setCERTCODE(bundle.getString("certCode"));
        user.setBrCode(bundle.getString("brCode"));
        userTitle.setText("欢迎登陆：" + userName);
        brNameTitle.setText("所属机构:" + brname);
        PreferencesUtils pre = new PreferencesUtils(this, "CommonInfo");
        pre.putString("userId", userId);

        if (null != pswlater && !"".equals(pswlater)) {
            new AlertDialog.Builder(FirstActivity.this).setTitle(pswlater).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            }).show();
        }

        manager = getFragmentManager();
        appStoreFragment = new AppStoreFragment(userId, mBlueToothAdapter,user);
        uploadLogFragment = new UploadLogFragment(FirstActivity.this);
        mFragmentTransaction = manager.beginTransaction();
        mFragmentTransaction.replace(R.id.centerRLO, appStoreFragment);
        tvAppStore.setBackgroundColor(getResources().getColor(R.color.button_choose));
        tvChangePassword.setBackgroundColor(getResources().getColor(R.color.button_first));
        tvEquipmentTest.setBackgroundColor(getResources().getColor(R.color.button_first));
        mFragmentTransaction.commit();
        //requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        //蓝牙连接模块
        blueToothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBlueToothAdapter = blueToothManager.getAdapter();
        checkOpenBlue(mBlueToothAdapter);
        getBondedDevices();
        receiverRegister();
        searchDevices();
        progressBar.setVisibility(View.VISIBLE);
        refreshDeviceImg.setVisibility(View.GONE);
        getVersion();
    }

    @Override
    public void Listener() {
        tvAppStore.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        closeApp.setOnClickListener(this);
        tvEquipmentTest.setOnClickListener(this);
        chooseDeviceName.setOnClickListener(this);
        refreshDeviceImg.setOnClickListener(this);
        tvLogUpload.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ToastUtil.showLongToast(getApplicationContext(), "退出请点击安全退出！");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("---destory---", "---destory---");
        stopService(exitService);
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(exitService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(exitService);
        //unregisterReceiver(mReceiver);
        //Toast.makeText(getApplicationContext(), "测试onpause", 2000).show();
    }

    //调用系统蓝牙功能，判断是否开启蓝牙
    private void checkOpenBlue(BluetoothAdapter mBlueToothAdapter) {
        if (null == mBlueToothAdapter || !mBlueToothAdapter.isEnabled()) {
            Intent BtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(BtIntent);
        }
    }

    @Override
    public void onClick(View v) {
        mFragmentTransaction = manager.beginTransaction();
        int Id = v.getId();
        tvAppStore.setBackgroundColor(getResources().getColor(R.color.button_choose));
        tvChangePassword.setBackgroundColor(getResources().getColor(R.color.button_first));
        tvEquipmentTest.setBackgroundColor(getResources().getColor(R.color.button_first));
        tvLogUpload.setBackgroundColor(getResources().getColor(R.color.button_first));
        switch (Id) {
            case R.id.tv_app_store:
                mFragmentTransaction.replace(R.id.centerRLO, appStoreFragment);
                tvAppStore.setBackgroundColor(getResources().getColor(R.color.button_choose));
                tvChangePassword.setBackgroundColor(getResources().getColor(R.color.button_first));
                tvEquipmentTest.setBackgroundColor(getResources().getColor(R.color.button_first));
                mFragmentTransaction.commit();
                break;
            case R.id.tv_change_password:
                mFragmentTransaction.replace(R.id.centerRLO, uploadLogFragment);
                tvChangePassword.setBackgroundColor(getResources().getColor(R.color.button_choose));
                tvAppStore.setBackgroundColor(getResources().getColor(R.color.button_first));
                tvEquipmentTest.setBackgroundColor(getResources().getColor(R.color.button_first));
                mFragmentTransaction.commit();
                break;

            case R.id.tv_equipment_test:
                equipmentTestFragment = new EquipmentTestFragment(deviceId);
                mFragmentTransaction.replace(R.id.centerRLO, equipmentTestFragment);
                tvEquipmentTest.setBackgroundColor(getResources().getColor(R.color.button_choose));
                tvAppStore.setBackgroundColor(getResources().getColor(R.color.button_first));
                tvChangePassword.setBackgroundColor(getResources().getColor(R.color.button_first));
                mFragmentTransaction.commit();
                break;
            case R.id.close_app:
                //Toast.makeText(this, "已退出程序！", 2000).show();
                new AlertDialog.Builder(FirstActivity.this).setTitle("是否确认退出").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;

            case R.id.choose_device_name:
                ShowCommonTypeWindow.showCommonTypeWindow(FirstActivity.this, blueDevice, chooseDeviceName, 1920, 1200);
                if (blueDevice.size() == 0) {
                    ToastUtil.showLongToast(FirstActivity.this, "暂未搜索到可用设备！正重新搜索");
                    getBondedDevices();
                    receiverRegister();
                    searchDevices();
                    progressBar.setVisibility(View.VISIBLE);
                    refreshDeviceImg.setVisibility(View.GONE);
                }
                break;

            case R.id.refresh_device_img:
                //ToastUtil.showLongToast(FirstActivity.this, "正重新搜索设备");
                getBondedDevices();
                receiverRegister();
                searchDevices();
                progressBar.setVisibility(View.VISIBLE);
                refreshDeviceImg.setVisibility(View.GONE);
                break;

            case R.id.tv_log_upload://日志上传 只能上传最近日志
                mFragmentTransaction.replace(R.id.centerRLO, uploadLogFragment);
                tvLogUpload.setBackgroundColor(getResources().getColor(R.color.button_choose));
                tvAppStore.setBackgroundColor(getResources().getColor(R.color.button_first));
                tvEquipmentTest.setBackgroundColor(getResources().getColor(R.color.button_first));
                mFragmentTransaction.commit();

                //Tool.deleteFileByDate(ConstantValue.LogTransdir);
                //uploadCrashLogFile();
                //uploadTransLog();
                //zipTransLog();
                break;


            default:
                break;
        }
    }

    private void receiverRegister() {
        //注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter1);
        //注册搜索完成时的receiver
        mFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter1);

        pReceiver = new ParingReceived();
        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(pReceiver, mFilter2);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索到的不是已经绑定的蓝牙设备
                //if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                String temp    = device.getName();
                String address = device.getAddress();
                tempAddress = address;
                int deviceType = device.getBluetoothClass().getMajorDeviceClass();
                Log.i("---", "---广播---" + temp);
                boolean canAdd = true;
                try {
                    BlueDeviceInfo deviceInfo = new BlueDeviceInfo();
                    if (temp.contains("P3520") || temp.contains("W9310")) {
                        tempPin = "1235";
                        searchWoqi = 1;
                        if (neededConWoqi == 1) {
                            String s = "已选握奇设备--" + temp;
                            deviceInfo.setBlueDeviceName(s);
                            deviceInfo.setBlueDeviceAddress(tempAddress);
                            if (blueDevice.size() == 0) {
                                blueDevice.add(deviceInfo);
                            } else {
                                for (int i = 0; i < blueDevice.size(); i++) {
                                    if (blueDevice.get(i).getBlueDeviceName().equals(s)) {
                                        //blueDevice.remove(i);
                                        //blueDevice.add(s);
                                        canAdd = false;
                                    }
                                }
                                if (canAdd == true) {
                                    blueDevice.add(deviceInfo);
                                }
                            }
                        } else {
                            getDevices(address);
                        }
                    } else if (temp.contains("GEIT") || temp.contains("rk312")) {
                        tempPin = "1234";
                        searchGuoguang = 1;
                        if (neededConGuoguang == 1) {
                            String s = "已选国光设备--" + temp;
                            deviceInfo.setBlueDeviceName(s);
                            deviceInfo.setBlueDeviceAddress(tempAddress);
                            if (blueDevice.size() == 0) {
                                blueDevice.add(deviceInfo);
                            } else {
                                for (int i = 0; i < blueDevice.size(); i++) {
                                    if (blueDevice.get(i).getBlueDeviceName().equals(s)) {
                                        /*blueDevice.remove(i);
                                        blueDevice.add(s);*/
                                        canAdd = false;
                                    }
                                }
                                if (canAdd == true) {
                                    blueDevice.add(deviceInfo);
                                }
                            }
                        } else {
                            getDevices(address);
                        }
                    } else if (temp.contains("SS-")) {
                        searchShensi = 1;
                        if (neededConShensi == 1) {
                            String s = "已选神思设备--" + temp;
                            deviceInfo.setBlueDeviceName(s);
                            deviceInfo.setBlueDeviceAddress(tempAddress);
                            if (blueDevice.size() == 0) {
                                blueDevice.add(deviceInfo);
                            } else {
                                for (int i = 0; i < blueDevice.size(); i++) {
                                    if (blueDevice.get(i).getBlueDeviceName().equals(s)) {
                                        /*blueDevice.remove(i);
                                        blueDevice.add(s);*/
                                        canAdd = false;
                                    }
                                }
                                if (canAdd == true) {
                                    blueDevice.add(deviceInfo);
                                }
                            }
                        } else {
                            getDevices(address);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getBondedDevices();
                if (blueDevice.size() == 0) {
                    Toast.makeText(FirstActivity.this, "未搜索到可用蓝牙设备，请重新搜索", Toast.LENGTH_SHORT);
                }
                //}
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminate(false);
                refreshDeviceImg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            refreshDeviceImg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    };

    private class ParingReceived extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice btDevice = mBlueToothAdapter.getRemoteDevice(tempAddress);
            try {
//包含jar包已提供了相关操作类
                ClsUtils.setPin(btDevice.getClass(), btDevice, tempPin);
                //ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getBondedDevices() {
        Set<BluetoothDevice> devices = mBlueToothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                String temp = bluetoothDevice.getName();
                Log.i("---", "---广播2---" + temp);
                if (temp.contains("P3520") || temp.contains("W9310")) {
                    bondedWoqi = 1;
                    //已配对，不需要重新连接
                    neededConWoqi = 1;
                    deviceId = DEVICE_WOQI_ID;
                } else if (temp.contains("GEIT")) {
                    bondedGuoguang = 1;
                    //已配对，不需要重新连接
                    neededConGuoguang = 1;
                    deviceId = DEVICE_GUOGUANG_ID;
                } else if (temp.contains("SS-")) {
                    bondedShensi = 1;
                    //已配对，不需要重新连接
                    neededConShensi = 1;
                    deviceId = DEVICE_SHENSI_ID;
                }
            }
            setButtonEnable();
        }
    }

    private void getDevices(String address) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.checkBluetoothAddress(address)) {//检查是否是有效的蓝牙地址
            Toast.makeText(FirstActivity.this, "蓝牙设备地址无效", Toast.LENGTH_SHORT).show();
        } else {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            if (device != null) {
                Boolean returnValue = null;
                try {
                    //ClsUtils.setPin(device.getClass(), device, "1235");
                    returnValue = ClsUtils.createBond(BluetoothDevice.class, device);
                    Log.i("---配对情况---", "---配对情况---" + returnValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (returnValue) {//发起配对成功,并不代表配对成功，因为可能被拒绝
                    Toast.makeText(FirstActivity.this, "蓝牙匹配成功！", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(FirstActivity.this, "蓝牙匹配失败！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FirstActivity.this, "蓝牙设备查询失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setButtonEnable() {
        //progressBar.setVisibility(View.GONE);
        if (searchGuoguang == 1 && bondedGuoguang == 1) {
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }

        if (searchWoqi == 1 && bondedWoqi == 1) {
            Message msg = new Message();
            msg.what = 3;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 4;
            mHandler.sendMessage(msg);
        }

        if (searchShensi == 1 && bondedShensi == 1) {
            Message msg = new Message();
            msg.what = 5;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 6;
            mHandler.sendMessage(msg);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //progressBar.setVisibility(View.GONE);
            if (msg.what == 1) {
                //ToastUtil.showLongToast(FirstActivity.this, "国光外设可连接！");
                deviceId = DEVICE_GUOGUANG_ID;
                //BtGuoguang.setEnabled(true);
            } else if (msg.what == 2) {
                //ToastUtil.showLongToast(FirstActivity.this, "国光外设不可连接！");
                //BtGuoguang.setEnabled(false);
            } else if (msg.what == 3) {
                //ToastUtil.showLongToast(FirstActivity.this, "握奇外设可连接！");
                deviceId = DEVICE_WOQI_ID;
                //BtWoqi.setEnabled(true);
            } else if (msg.what == 4) {
                //BtWoqi.setEnabled(false);
            } else if (msg.what == 5) {
                //ToastUtil.showLongToast(FirstActivity.this, "神思外设可连接！");
                deviceId = DEVICE_SHENSI_ID;
                //BtShensi.setEnabled(true);
            } else if (msg.what == 6) {
                //BtShensi.setEnabled(false);
            }
        }
    };

    private void searchDevices() {
        if (mBlueToothAdapter.isDiscovering()) {
            mBlueToothAdapter.cancelDiscovery();
        }
        mBlueToothAdapter.startDiscovery();
    }


    /**
     * 获取本地版本
     *
     * @return
     */
    public void getVersion() {

        PackageManager manager  = this.getPackageManager();
        PackageInfo    packInfo =null ;
        try {
            packInfo=  manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo != null) {
            tv_version_num.setText("版本名称：" + packInfo.versionName+"     版本号：" + packInfo.versionCode);
        }
    }

}
