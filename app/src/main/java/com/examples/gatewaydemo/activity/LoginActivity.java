package com.examples.gatewaydemo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.examples.gatewaydemo.base.BaseActivity;
import com.examples.gatewaydemo.fragment.AllApplicationFragment;
import com.examples.gatewaydemo.httputils.HttpGetUtil;
import com.examples.gatewaydemo.model.AppInfo;
import com.examples.gatewaydemo.model.BlueDeviceInfo;
import com.examples.gatewaydemo.model.RelationInfo;
import com.examples.gatewaydemo.model.UploadInfo;
import com.examples.gatewaydemo.model.User;
import com.examples.gatewaydemo.util.ApkUpdater;
import com.examples.gatewaydemo.util.ConstantValue;
import com.examples.gatewaydemo.util.DateFormatUtil;
import com.examples.gatewaydemo.util.LogUtils;
import com.examples.gatewaydemo.util.PermissionUtils;
import com.examples.gatewaydemo.util.PreferencesUtils;
import com.examples.gatewaydemo.util.ShowCommonTypeWindow;
import com.examples.gatewaydemo.util.ShowCommonWindow;
import com.examples.gatewaydemo.util.ToastUtil;
import com.examples.gatewaydemo.util.Tool;
import com.examples.gatewaydemo.util.UrlUtil;
import com.examples.outputjar.BlueUtils;
import com.examples.outputjar.ClsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LoginActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {
    private       EditText      userNameEd;
    private       EditText      passwordEd;
    private       TextView      serialNoTv;
    private       Button        loginBt;
    private final int           LoginSuccess       = 8;
    private final int           LoginFailure       = 9;
    private final int           RequestSuccess     = 20;
    private final int           ResponseFailure    = 21;
    private final int           requestTestSuccess = 22;
    private final int           requestTestFailure = 23;
    private       String        NEW_LOGIN          = "T000001";// ??????
    private       MyApplication application;
    private       ProgressBar   progressBar;
    private       LinearLayout  userNameEdLr, passwordEdLr;
    private String   serialNo;
    private TextView changeNetInfoEt;
    private Button   chooseNetInfoBt;
    private TextView chooseDeviceName, versionNameTx;
    private       ImageView            refreshDeviceImg;
    private       List<BlueDeviceInfo> blueDevice         = new ArrayList<BlueDeviceInfo>();
    //????????????
    private final int                  DEVICE_GUOGUANG_ID = 1;
    private final int                  DEVICE_SHENSI_ID   = 2;
    private final int                  DEVICE_WOQI_ID     = 3;
    private       int                  deviceId;
    private       ParingReceived       pReceiver;
    private       String               tempAddress;
    private       String               tempPin;
    private       int                  searchGuoguang, searchShensi, searchWoqi;
    private int bondedGuoguang, bondedShensi, bondedWoqi;
    private int neededConGuoguang, neededConShensi, neededConWoqi;
    private BluetoothManager        blueToothManager;
    private BluetoothAdapter        mBlueToothAdapter;
    private MyTextWatcher           watcher;
    private PreferencesUtils        pre;
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    String retCode = "";//?????????????????????
    //private List<RelationInfo> relationInfos=new ArrayList<RelationInfo>();
    private String  brCode       = "";
    private String  pasLater     = "";
    private boolean mReceiverTag = false;

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.new_activity_login);
        userNameEd = (EditText) findViewById(R.id.login_edit_user);
        passwordEd = (EditText) findViewById(R.id.login_edit_psd);
        userNameEdLr = (LinearLayout) findViewById(R.id.login_edit_user_lr);
        passwordEdLr = (LinearLayout) findViewById(R.id.login_edit_psd_lr);
        serialNoTv = (TextView) findViewById(R.id.tv_device_pad_serialno);
        loginBt = (Button) findViewById(R.id.login_btn);
        chooseDeviceName = (TextView) findViewById(R.id.choose_device_name);
        //progressBar=(ProgressBar) findViewById(R.id.refresh_progressBar);
        refreshDeviceImg = (ImageView) findViewById(R.id.refresh_device_img);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        changeNetInfoEt = (TextView) findViewById(R.id.change_net_info);
        chooseNetInfoBt = (Button) findViewById(R.id.choose_net_info_Bt);
        versionNameTx = (TextView) findViewById(R.id.tv_version);
        application = MyApplication.getInstance();
        userNameEd.setText("001810");
        passwordEd.setText("a111111");
        versionNameTx.setText("v_" + Tool.getVersionName(LoginActivity.this));
//		userNameEd.setText("A002");
//		passwordEd.setText("111111YY");
		/*try {
			serialNo=Tool.getIMEI(LoginActivity.this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//serialNo=Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		if(serialNo!=null){
			serialNoTv.setText("????????????"+serialNo);
		}*/
        //??????????????????
        blueToothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBlueToothAdapter = blueToothManager.getAdapter();
        String s = Tool.getSerialNo(LoginActivity.this);
        serialNo = s;
        LogUtils.d("?????????????????????=" + s);
        serialNoTv.setText("????????????" + serialNo);
        //progressBar.setVisibility(View.VISIBLE);
        watcher = new MyTextWatcher();
        chooseDeviceName.addTextChangedListener(watcher);
        pre = new PreferencesUtils(LoginActivity.this, "BlueTooth");
        PermissionUtils.verrifyStoragePermissions(LoginActivity.this);
        requestTest();
        requestForList();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestSuccess:  //??????????????????
                    //Gson gson=new Gson();
                    String jOb = (String) msg.obj;
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject dataJsonObject = null;
                    JSONArray dataJsonArray = null;

                    if (jOb.startsWith("[")) {
                        try {
                            dataJsonArray = new JSONArray(jOb);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            dataJsonObject = new JSONObject(jOb);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    readJson(dataJsonObject);

                    break;
                case ResponseFailure://?????????????????? ???????????????????????????
                    ToastUtil.showLongToast(LoginActivity.this, "???????????????????????????????????????????????????");
                    moveToVPN();
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                case requestTestSuccess:
                    break;
                case requestTestFailure:
                    moveToVPN();
                    break;
                //????????????
                case LoginSuccess:
                    User user = new User();
                    user = (User) msg.obj;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    //bundle.putString("userId", user.getUserid());
                    if (null != pasLater) {
                        bundle.putString("pswlater", pasLater);
                    } else {
                        bundle.putString("pswlater", "");
                    }
                    bundle.putString("userName", user.getUsername());
                    bundle.putString("brName", user.getBrName());
                    bundle.putString("brCode",user.getBrCode());
                    bundle.putString("certCode",user.getCERTCODE());
                    intent.putExtras(bundle);
                    intent.setClass(getApplicationContext(), FirstActivity.class);
                    startActivity(intent);
                    break;
                case LoginFailure:
                    shwoLoginFailStadus(retCode);
                    /*Intent intent1 = new Intent();
                    Bundle bundle1 = new Bundle();
                    //bundle.putString("userId", user.getUserid());
                    bundle1.putString("userName", "heihie");
                    bundle1.putString("brName", "test");
                    intent1.putExtras(bundle1);
                    intent1.setClass(getApplicationContext(), FirstActivity.class);
                    startActivity(intent1);*/
                    break;
                case 0:
                    int flag = 0;
                    String macAddress = pre.getString("macAddress", "");
                    if (chooseDeviceName.getText().toString().contains("??????")) {
                        flag = BlueUtils.DEVICE_WOQI_ID;
                    } else if (chooseDeviceName.getText().toString().contains("??????")) {
                        flag = BlueUtils.DEVICE_SHENSI_ID;
                    } else if (chooseDeviceName.getText().toString().contains("??????")) {
                        flag = BlueUtils.DEVICE_GUOGUANG_ID;
                    }
                    BlueUtils.GetDeviceId(flag, LoginActivity.this, handler, hashMap);
                    break;
                case 12:
                    Toast.makeText(LoginActivity.this, "???????????????????????????", Toast.LENGTH_LONG).show();
                    //String s=hashMap.get("deviceId");
                    String s = Tool.getIMEI(LoginActivity.this);
                    //serialNoTv.setText("????????????"+s);
                    break;
                case 13:
                    Toast.makeText(LoginActivity.this, "???????????????????????????", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }

        }

        /**
         *   //???????????????????????????????????????
         *   add by hutian 2018-07-09
         * @param Stadus
         */
        private void shwoLoginFailStadus(String Stadus) {
            if (null != retCode && retCode.equals("1016")) {
                ToastUtil.showLongToast(LoginActivity.this, "???????????????");
            } else if (null != retCode && retCode.equals("1017")) {
                ToastUtil.showLongToast(LoginActivity.this, "??????????????????");
            } else if (null != retCode && retCode.equals("1018")) {
                ToastUtil.showLongToast(LoginActivity.this, "????????????");
            } else if (null != retCode && retCode.equals("1019")) {
                ToastUtil.showLongToast(LoginActivity.this, "?????????????????????");
            } else if (null != retCode && retCode.equals("1033")) {
                ToastUtil.showLongToast(LoginActivity.this, "?????????????????????????????????????????????????????????");
            } else if (null != retCode && retCode.equals("1035")) {
                ToastUtil.showLongToast(LoginActivity.this, "??????????????????????????????");
            } else if (null != retCode && retCode.equals("1013")) {
                ToastUtil.showLongToast(LoginActivity.this, "?????????????????????????????????????????????????????????");
            } else if (null != retCode && retCode.equals("1020")) {
                ToastUtil.showLongToast(LoginActivity.this, "????????????????????????BMC?????????");
            } else if (null != retCode && retCode.equals("1028")) {
                ToastUtil.showLongToast(LoginActivity.this, "???????????????");
            } else {
                Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_LONG).show();
            }

        }

        ;
    };

    private Handler textHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 12:
                    Toast.makeText(LoginActivity.this, "???????????????????????????", Toast.LENGTH_LONG).show();
                    String s = hashMap.get("deviceId");
                    //	serialNoTv.setText(s);
                    break;
                case 13:
                    Toast.makeText(LoginActivity.this, "???????????????????????????", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void readJson(JSONObject tepJsonObject) {
        if (tepJsonObject.has("retCode")) {
            try {
                User user = new User();
                retCode = tepJsonObject.getString("retCode");
                brCode = tepJsonObject.getString("brCode");
                pasLater = tepJsonObject.getString("PSWDALTER");
                user.setUsername(tepJsonObject.getString("userName"));
                user.setBrName(tepJsonObject.getString("brName"));
                user.setBrCode(tepJsonObject.getString("brCode"));
                user.setCERTCODE(tepJsonObject.getString("CERTCODE"));
                JSONArray dataJsonArray = tepJsonObject.getJSONArray("teller_ID");
                String    jsonArray     = dataJsonArray.toString();
                LogUtils.d("?????????????????????=" + jsonArray);
                JSONArray array = new JSONArray();
                //relationInfos= Tool.parseList(jsonArray,RelationInfo.class);
                //??????????????????????????????,???????????????????????????RelationInfo?????????
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    JSONObject   toJSONObject = dataJsonArray.getJSONObject(i);
                    RelationInfo info         = new RelationInfo();
                    info.setTellId(toJSONObject.getString("TELLERID"));
                    info.setBrCode(toJSONObject.getString("ORG_CODE"));
                    info.setSystemName(toJSONObject.getString("SYSTEMNAME"));
                    ConstantValue.relationInfos.add(info);
                }
                //????????????????????????
                //user.setBrCode(tepJsonObject.getString("brCode"));
                if (null != retCode && retCode.equals("0000")) {//??????????????????
                    Message msg = new Message();
                    msg.what = LoginSuccess;
                    msg.obj = user;
                    handler.sendMessage(msg);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Message msg = new Message();
                msg.obj = retCode;
                msg.what = LoginFailure;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Listener() {
        // TODO Auto-generated method stub
        loginBt.setOnClickListener(this);
        //userNameEd.setOnFocusChangeListener(this);
        //passwordEd.setOnFocusChangeListener(this);
        chooseNetInfoBt.setOnClickListener(this);
        chooseDeviceName.setOnClickListener(this);
        refreshDeviceImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int Id = v.getId();
        switch (Id) {
            case R.id.login_btn:
             getApp();
                break;

            case R.id.choose_net_info_Bt:
                ShowCommonWindow.showCommonTypeWindow(LoginActivity.this, UrlUtil.netInfo, changeNetInfoEt, 1500, 800);
                break;
            case R.id.choose_device_name:
                ShowCommonTypeWindow.showCommonTypeWindow(LoginActivity.this, blueDevice, chooseDeviceName, 1920, 1200);
                if (blueDevice.size() == 0) {
                    ToastUtil.showLongToast(LoginActivity.this, "?????????????????????????????????????????????");
                    getBondedDevices();

                    receiverRegister();
                    searchDevices();
                    //progressBar.setVisibility(View.VISIBLE);
                    refreshDeviceImg.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void Login() {
        if (changeNetInfoEt.getText().toString().trim() != null && !changeNetInfoEt.getText().toString().trim().equals("")) {
            if (changeNetInfoEt.getText().toString().trim().contains("??????")) {
                UrlUtil.netType = 1;
            } else if (changeNetInfoEt.getText().toString().trim().contains("??????")) {
                UrlUtil.netType = 2;
            } else if (changeNetInfoEt.getText().toString().trim().contains("??????")) {
                UrlUtil.netType = 3;
            } else { //????????? ??????????????????
                UrlUtil.SERVICE_IP = changeNetInfoEt.getText().toString().trim();
            }
        }
//			Intent intent=new Intent(this,FirstActivity.class);
//			startActivity(intent);
        //https://9.1.80.23:8081/svn/newSVN
        Log.i("---test---", "---test---");
        //String serialNo=Tool.getSerialNo();
        //serialNo="Q4HNU18319114803";//????????????  //MWUBB17526203611  F9FQLBWDFCM8
        if (null != userNameEd.getText().toString().trim() && null != passwordEd.getText().toString().trim()) {
            String username = userNameEd.getText().toString().trim();
            String password = passwordEd.getText().toString().trim();
            if ("".equals(username)) {
                Toast.makeText(getApplicationContext(), "???????????????????????????", Toast.LENGTH_LONG).show();
            } else if ("".equals(password)) {
                Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
            } else {
                if (Tool.isFastDouleClick()) {
                    HttpGetUtil util = new HttpGetUtil(LoginActivity.this);

                    password = Tool.ecodeByMD5(password);
                    progressBar.setVisibility(View.VISIBLE);
                    String url = UrlUtil.getServiceIp() + "services/" + NEW_LOGIN + "?method=getJSON"
                            + "&transCode=" + NEW_LOGIN
                            + "&userCode=" + username
                            + "&password=" + password
                            + "&seriNo=" + serialNo
                            //+ "&seriNo=" + "Q4HNU18319113304"
                            + "&chnlCode=" + "01";
                    util.HttpGetRequest(url, handler, RequestSuccess, ResponseFailure);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        int Id = v.getId();
        switch (Id) {
            case R.id.login_edit_user:
                if (hasFocus) {
                    userNameEdLr.setBackgroundDrawable(getDrawable(R.drawable.login_input_background_focus));
                } else {
                    userNameEdLr.setBackgroundDrawable(getDrawable(R.drawable.login_input_background_unfocus));
                }
                break;

            case R.id.login_edit_psd:
                if (hasFocus) {
                    passwordEdLr.setBackgroundDrawable(getDrawable(R.drawable.login_input_background_focus));
                } else {
                    passwordEdLr.setBackgroundDrawable(getDrawable(R.drawable.login_input_background_focus));
                }

            default:
                break;
        }
    }

    private void moveToVPN() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("cn.maipu.vpn");
        if (intent == null) {
            ToastUtil.showLongToast(LoginActivity.this, "????????????vpn????????????");
        } else {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            startActivity(intent);
        }
    }

    //??????????????????
    private void requestTest() {
        HttpGetUtil util = new HttpGetUtil(LoginActivity.this);
        //progressBar.setVisibility(View.VISIBLE);
        String url = UrlUtil.getServiceIp() + "services/" + "Fkezs?method=getJSON";
        //progressBar.setVisibility(View.VISIBLE);
        util.HttpGetRequest(url, handler, 2, 3);
    }

    //???????????????????????????????????????????????????
    private void checkOpenBlue(BluetoothAdapter mBlueToothAdapter) {
        // TODO Auto-generated method stub
        if (null == mBlueToothAdapter || !mBlueToothAdapter.isEnabled()) {
            Intent BtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(BtIntent);
        }
    }

    private void receiverRegister() {
        // TODO Auto-generated method stub
        //???????????????????????????????????????????????????receiver
        mReceiverTag = true;
        IntentFilter mFilter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter1);
        //????????????????????????receiver
        mFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter1);

        pReceiver = new ParingReceived();
        IntentFilter mFilter2 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(pReceiver, mFilter2);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("---destory---", "---destory---");
        if (mReceiverTag) {
            mReceiverTag = false;
            unregisterReceiver(mReceiver);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //?????????????????????????????????????????????
                //if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                String temp    = device.getName();
                String address = device.getAddress();
                tempAddress = address;
                int deviceType = device.getBluetoothClass().getMajorDeviceClass();
                Log.i("---", "---??????---" + temp);
                boolean canAdd = true;
                try {
                    BlueDeviceInfo deviceInfo = new BlueDeviceInfo();
                    if (temp.contains("P3520") || temp.contains("W9310")) {
                        tempPin = "1235";
                        searchWoqi = 1;
                        if (neededConWoqi == 1) {
                            String s = "??????????????????--" + temp;
                            deviceInfo.setBlueDeviceName(s);
                            deviceInfo.setBlueDeviceAddress(tempAddress);
                            Log.i("------", "---3---macAddress---" + tempAddress);
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
                            String s = "??????????????????--" + temp;
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
                            String s = "??????????????????--" + temp;
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
                    // TODO: handle exception
                    e.printStackTrace();
                }

                getBondedDevices();
                if (blueDevice.size() == 0) {
                    Toast.makeText(LoginActivity.this, "????????????????????????????????????????????????", Toast.LENGTH_LONG);
                }
                //}
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminate(false);
                //??????????????????
                //refreshDeviceImg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            //??????????????????
            //refreshDeviceImg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    };

    private class ParingReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            BluetoothDevice btDevice = mBlueToothAdapter.getRemoteDevice(tempAddress);
            try {
//??????jar??????????????????????????????
                ClsUtils.setPin(btDevice.getClass(), btDevice, tempPin);
                //ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void getBondedDevices() {
        Set<BluetoothDevice> devices = mBlueToothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                String temp = bluetoothDevice.getName();
                Log.i("---", "---??????2---" + temp);
                if (temp.contains("P3520") || temp.contains("W9310")) {
                    bondedWoqi = 1;
                    //?????????????????????????????????
                    neededConWoqi = 1;
                    deviceId = DEVICE_WOQI_ID;
                } else if (temp.contains("GEIT")) {
                    bondedGuoguang = 1;
                    //?????????????????????????????????
                    neededConGuoguang = 1;
                    deviceId = DEVICE_GUOGUANG_ID;
                } else if (temp.contains("SS-")) {
                    bondedShensi = 1;
                    //?????????????????????????????????
                    neededConShensi = 1;
                    deviceId = DEVICE_SHENSI_ID;
                }
            }
            setButtonEnable();
        }
    }

    private void getDevices(String address) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.checkBluetoothAddress(address)) {//????????????????????????????????????
            Toast.makeText(LoginActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
        } else {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            if (device != null) {
                Boolean returnValue = null;
                try {
                    //ClsUtils.setPin(device.getClass(), device, "1235");
                    returnValue = ClsUtils.createBond(BluetoothDevice.class, device);
                    Log.i("---????????????---", "---????????????---" + returnValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (returnValue) {//??????????????????,????????????????????????????????????????????????
                    Toast.makeText(LoginActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
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
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            //progressBar.setVisibility(View.GONE);
            if (msg.what == 1) {
                //ToastUtil.showLongToast(LoginActivity.this, "????????????????????????");
                deviceId = DEVICE_GUOGUANG_ID;
                //BtGuoguang.setEnabled(true);
            } else if (msg.what == 2) {
                //ToastUtil.showLongToast(LoginActivity.this, "???????????????????????????");
                //BtGuoguang.setEnabled(false);
            } else if (msg.what == 3) {
                //ToastUtil.showLongToast(LoginActivity.this, "????????????????????????");
                deviceId = DEVICE_WOQI_ID;
                //BtWoqi.setEnabled(true);
            } else if (msg.what == 4) {
                //BtWoqi.setEnabled(false);
            } else if (msg.what == 5) {
                //ToastUtil.showLongToast(LoginActivity.this, "????????????????????????");
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

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int    flag       = 0;
            String macAddress = pre.getString("macAddress", "");
            Log.i("---------", "---1---macAddress-----" + macAddress);
            String temp = charSequence.toString();
            if (temp.contains("??????")) {
                flag = BlueUtils.DEVICE_WOQI_ID;
            } else if (temp.contains("??????")) {
                flag = BlueUtils.DEVICE_SHENSI_ID;
            } else if (temp.contains("??????")) {
                flag = BlueUtils.DEVICE_GUOGUANG_ID;
            }

            try {
                BlueUtils.ConnectDevice(flag, LoginActivity.this, handler, macAddress);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Tool.ShowToast(LoginActivity.this, e.toString() + "---" + macAddress);
                e.printStackTrace();
            }
        }
    }


    private static final int               REQUEST_SUCCESS = 0;
    private static final int               REQUEST_FAILURE = 3;
    private static final int               PROCESSING      = 1;
    private static final int               FAILURE         = -1;
    private              PackageManager    pageManager;
    private              List<PackageInfo> packages;
private  UploadInfo mUploadInfo;
    private void requestForList() {
        HttpGetUtil util = new HttpGetUtil(this);

        //progressBar.setVisibility(View.VISIBLE);
        String url = UrlUtil.getServiceIp() + "services/" + "Fkezs?method=getJSON&SOFT_TYPE=0";
        util.HttpGetRequest(url, handlerRequest, REQUEST_SUCCESS, REQUEST_FAILURE);
    }


    private Handler handlerRequest = new UIHandler();

    private final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESSING: // ????????????
//					progressBar.setProgress(msg.getData().getInt("size"));
//					float num = (float) progressBar.getProgress() / (float) progressBar.getMax();
//					int result = (int) (num * 100); // ????????????
//					//resultView.setText(result + "%");
//					if (progressBar.getProgress() == progressBar.getMax()) {
//						Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_LONG).show();
//						exit();
//						progressBar.setVisibility(View.GONE);
//						pre.putInt("downloadPosition", 99);
//						whetherDownload = 0;
//					}
                    break;
                case FAILURE: // ????????????
//					Toast.makeText(getActivity(), "????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
//					pre.putInt("downloadPosition", 99);
//					whetherDownload = 0;
                    break;

                case REQUEST_SUCCESS:
                    String jOb = (String) msg.obj;
				/*JSONObject dataJsonObject = null;
                JSONArray dataJsonArray = null;

                if (jOb.startsWith("[")) {
                    try {
						dataJsonArray = new JSONArray(jOb);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                    try {
						dataJsonObject = new JSONObject(jOb);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }*/

                    List<UploadInfo> appInfoList = Tool.parseList(jOb, UploadInfo.class);
//					adapter = new AllApplicationFragment.AllApplicationAdapter(appInfoList, getActivity());
//					getInfoProgressBar.setVisibility(View.GONE);
//					listView.setAdapter(adapter);
//					//readJson(dataJsonObject);
                  mUploadInfo=appInfoList.get(0);
                  getApp();
                    break;

                case REQUEST_FAILURE:
                    ToastUtil.showLongToast(LoginActivity.this, "?????????????????????");
                    break;
            }
        }
    }


    private void getApp() {
        // TODO Auto-generated method stub
        pageManager = getPackageManager();
        packages = pageManager.getInstalledPackages(0);
        // ????????????
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            //if(packageInfo.packageName.contains("gatewaytest")){
            if (packageInfo.packageName.contains("com.examples.gatewaydemo")) {

                if(mUploadInfo!=null) {

                    if (mUploadInfo.getVERSIONCODE() > packageInfo.versionCode) {
                        new AlertDialog.Builder(this)
                                .setTitle("???????????????")
                                .setMessage("?????????????????????:" + mUploadInfo.getVERSIONCODE() + "   ???????????????" + mUploadInfo.getSOFT_VERSION() +
                                        "\n????????????????????????:" + packageInfo.versionCode + "   ???????????????" + packageInfo.versionName + "\n???????????????!!!")
                                .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        //pd.setMessage("??????????????? " + uploadInfo.getVersionNo() + "?????????????????????");
                                        pd.setCancelable(false);
                                        pd.show();
                                        //requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                                        ApkUpdater updater = new ApkUpdater(pd, mUploadInfo, LoginActivity.this, handler);
                                        updater.start();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    } else {
                        Login();
                    }
                }else{
                    Login();
                }
            }
        }
    }

}
