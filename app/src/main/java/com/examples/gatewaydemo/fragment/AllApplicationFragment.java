package com.examples.gatewaydemo.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.examples.gatewaydemo.aboutdownload.DownloadProgressListener;
import com.examples.gatewaydemo.aboutdownload.FileDownloader;
import com.examples.gatewaydemo.base.BaseFragment;
import com.examples.gatewaydemo.httputils.HttpGetUtil;
import com.examples.gatewaydemo.model.AppInfo;
import com.examples.gatewaydemo.model.RelationInfo;
import com.examples.gatewaydemo.model.UploadInfo;
import com.examples.gatewaydemo.model.User;
import com.examples.gatewaydemo.util.ApkUpdater;
import com.examples.gatewaydemo.util.ConstantValue;
import com.examples.gatewaydemo.util.PreferencesUtils;
import com.examples.gatewaydemo.util.ToastUtil;
import com.examples.gatewaydemo.util.Tool;
import com.examples.gatewaydemo.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class AllApplicationFragment extends BaseFragment implements OnClickListener {
    private View                  view;
    private ListView              listView;
    private ProgressBar           progressBar;
    private ProgressBar           getInfoProgressBar;
    //private List<AppInfo> appInfoList;
    private AllApplicationAdapter adapter;
    private AppInfo               info, info2, info3, info4, info5, info6, info7, info8;
    private              PackageInfo       packageInfo;
    private              PackageManager    pageManager;
    private              List<PackageInfo> packages;
    private              PreferencesUtils  pre;
    private              String            collecString;
    private              String            userId;
    private static final int               PROCESSING      = 1;
    private static final int               FAILURE         = -1;
    private static final int               REQUEST_SUCCESS = 0;
    private static final int               REQUEST_FAILURE = 3;
    private              int               whetherDownload = 0;
    private              List<UploadInfo>  appInfoList;
    private              BluetoothAdapter  mBlueToothAdapter;
    private User user;
	/*private static final int REQUEST_EXTERNAL_STORAGE =1;
	private static String[] PERMISSIONS_STORAGE={
		"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"
	};*/

    public AllApplicationFragment() {
        // TODO Auto-generated constructor stub
    }

    public AllApplicationFragment(String userId, BluetoothAdapter mBlueToothAdapter,User user) {
        this.userId = userId;
        this.mBlueToothAdapter = mBlueToothAdapter;
        this.user=user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_all_application, null);
        initView();
        Listener();
        return view;
    }

    private Handler handler = new UIHandler();

    private final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESSING: // 更新进度
                    progressBar.setProgress(msg.getData().getInt("size"));
                    float num = (float) progressBar.getProgress() / (float) progressBar.getMax();
                    int result = (int) (num * 100); // 计算进度
                    //resultView.setText(result + "%");
                    if (progressBar.getProgress() == progressBar.getMax()) {
                        Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_LONG).show();
                        exit();
                        progressBar.setVisibility(View.GONE);
                        pre.putInt("downloadPosition", 99);
                        whetherDownload = 0;
                    }
                    break;
                case FAILURE: // 下载失败
                    Toast.makeText(getActivity(), "下载更新失败，连接超时或网络异常", Toast.LENGTH_LONG).show();
                    pre.putInt("downloadPosition", 99);
                    whetherDownload = 0;
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

                    appInfoList = Tool.parseList(jOb, UploadInfo.class);
                    adapter = new AllApplicationAdapter(appInfoList, getActivity());
                    getInfoProgressBar.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                    //readJson(dataJsonObject);
                    break;

                case REQUEST_FAILURE:
                    ToastUtil.showLongToast(getActivity(), "数据访问失败！");
                    break;
            }
        }
    }

    @Override
    public void initView() {
        pre = new PreferencesUtils(getActivity(), "collectionInfo");
        pre.putInt("downloadPosition", 99);
        listView = (ListView) view.findViewById(R.id.all_application_listview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        getInfoProgressBar = (ProgressBar) view.findViewById(R.id.getRequestProgressBar);
        requestForList();
        //getApp();
        //appInfoList = new ArrayList<AppInfo>();
    }

    private void readJson(JSONObject tepJsonObject) {
        if (tepJsonObject.has("retCode")) {
            try {
                User   user    = new User();
                String retCode = tepJsonObject.getString("retCode");
                //根据需求放置参数
                user.setBrCode(tepJsonObject.getString("brCode"));
                if (null != retCode && retCode.equals("0000")) {
                    Message msg = new Message();
                    msg.what = 8;
                    msg.obj = user;
                    handler.sendMessage(msg);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Listener() {
        // TODO Auto-generated method stub
        progressBar.setOnClickListener(this);
    }

    private void requestForList() {
        HttpGetUtil util = new HttpGetUtil(getActivity());

        //progressBar.setVisibility(View.VISIBLE);
        String url = UrlUtil.getServiceIp() + "services/" + "Fkezs?method=getJSON";
        getInfoProgressBar.setVisibility(View.VISIBLE);
        util.HttpGetRequest(url, handler, REQUEST_SUCCESS, REQUEST_FAILURE);
    }

    public class AllApplicationAdapter extends BaseAdapter {
        private List<UploadInfo> appInfoList;
        private Context          context;

        public AllApplicationAdapter(List<UploadInfo> appInfoList, Context context) {
            this.appInfoList = appInfoList;
            this.context = context;
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
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = null;
            if (null == convertView) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_all_application_listview, null);
                holder.appName = (TextView) convertView.findViewById(R.id.appName);
                holder.appRemark = (TextView) convertView.findViewById(R.id.appRemark);
                holder.appVersion = (TextView) convertView.findViewById(R.id.appVersion);
                holder.appVersionCode = (TextView) convertView.findViewById(R.id.appVersionCode);
                holder.updateTime = (TextView) convertView.findViewById(R.id.appUpdateTime);
                holder.collectionApp = (Button) convertView.findViewById(R.id.collection_app);
                holder.deleteApp = (Button) convertView.findViewById(R.id.delete_app);
                holder.updateApp = (Button) convertView.findViewById(R.id.update_app);
                holder.appSrc = (ImageView) convertView.findViewById(R.id.appSrc);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            if (null != appInfoList.get(position).getSOFT_CHINESS_NAME() && !"".equals(appInfoList.get(position).getSOFT_CHINESS_NAME())) {
                holder.appName.setText(appInfoList.get(position).getSOFT_CHINESS_NAME());
            }
            if (null != appInfoList.get(position).getDESCRIBE() && !"".equals(appInfoList.get(position).getDESCRIBE())) {
                holder.appRemark.setText(appInfoList.get(position).getDESCRIBE());
            }
            if (null != appInfoList.get(position).getSOFT_VERSION() && !"".equals(appInfoList.get(position).getSOFT_VERSION())) {
                holder.appVersion.setText(appInfoList.get(position).getSOFT_VERSION());
            }
            holder.appVersionCode.setText(appInfoList.get(position).getVERSIONCODE() + "");

            if (null != appInfoList.get(position).getUPDATETIME() && !"".equals(appInfoList.get(position).getUPDATETIME())) {
                holder.updateTime.setText(appInfoList.get(position).getUPDATETIME());
            }
            if (null != appInfoList.get(position).getICON_FLAG() && !"".equals(appInfoList.get(position).getICON_FLAG())) {
                if ("1".equals(appInfoList.get(position).getICON_FLAG())) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon01));
                } else if ("2".equals(appInfoList.get(position).getICON_FLAG())) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon02));
                } else if ("3".equals(appInfoList.get(position).getICON_FLAG())) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon03));
                } else if ("4".equals(appInfoList.get(position).getICON_FLAG())) {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon04));
                } else {
                    holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon01));
                }
            } else {
                holder.appSrc.setImageDrawable(getResources().getDrawable(R.drawable.icon01));
            }

            try {
                packageInfo = context.getPackageManager().getPackageInfo(appInfoList.get(position).getPACKAGE_NAME(), PackageManager.GET_ACTIVITIES);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String s       = appInfoList.get(position).getSOFT_VERSION();
            int    version = appInfoList.get(position).getVERSIONCODE();
            String path    = appInfoList.get(position).getSOFT_PATH();
            if (packageInfo != null) {
                int n = packageInfo.versionCode;
                if (n >= version) {
                    holder.updateApp.setText("已下载");
                }
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        packageInfo = context.getPackageManager().getPackageInfo(appInfoList.get(position).getPACKAGE_NAME(), PackageManager.GET_ACTIVITIES);
                    } catch (PackageManager.NameNotFoundException e) {
                        packageInfo = null;
                        e.printStackTrace();
                    }
                    if (packageInfo == null) {
                        Toast.makeText(getActivity(), "应用未安装，请下载安装之后再进行此操作！", Toast.LENGTH_LONG).show();
                    } else {
                        String s       = appInfoList.get(position).getSOFT_VERSION();
                        int    n       = packageInfo.versionCode;
                        int    version = appInfoList.get(position).getVERSIONCODE();
                        if (n < version) {
                            ToastUtil.showLongToast(context, "应用有更新，正在下载更新！");
                            ProgressDialog pd = new ProgressDialog(getActivity());
                            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            //pd.setMessage("有新的版本 " + uploadInfo.getVersionNo() + "，正在下载更新");
                            pd.setCancelable(false);
                            pd.show();
                            //requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

                            ApkUpdater updater = new ApkUpdater(pd, appInfoList.get(position), getActivity(), handler);
                            updater.start();
                        } else {
                            if (mBlueToothAdapter != null) {
                                if (mBlueToothAdapter.enable()) {
                                    mBlueToothAdapter.disable();//关闭设备连接
                                }
                            }
                            moveTo(position, userId);
                        }
                    }

                }
            });

            holder.collectionApp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "收藏成功！", Toast.LENGTH_LONG).show();
                    collecString += appInfoList.get(position).getPACKAGE_NAME();
                    pre.putString("collecString", collecString);
                }
            });

            holder.deleteApp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Uri uri = Uri.parse("package:"
                            + appInfoList.get(position).getPACKAGE_NAME());
                    Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                    context.startActivity(intent);
                }
            });
            //新增已下载是 不再下载
            if (!"已下载".equals(holder.updateApp.getText())) {
//                ToastUtil.showShortToast(getActivity(), "你已经下载，无需重复下载");
//            } else {
                holder.updateApp.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
					/*int downloadPosition=pre.getInt("downloadPosition", 99);
					if(downloadPosition==99){
						progressBar.setVisibility(View.VISIBLE);
						String path="http://abv.cn/music/光辉岁月.mp3";
						String filename = path.substring(path.lastIndexOf('/') + 1);

						try {
							// URL编码（这里是为了将中文进行URL编码）
							filename = URLEncoder.encode(filename, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}

						path = path.substring(0, path.lastIndexOf("/") + 1) + filename;
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							// File savDir =
							// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
							// 保存路径
							File savDir = Environment.getExternalStorageDirectory();
							download(path, savDir);
							whetherDownload=1;
							pre.putInt("downloadPosition", position);
						} else {
							Toast.makeText(getActivity(),
									R.string.sdcarderror, Toast.LENGTH_LONG).show();
						}
						//holder.updateApp.setEnabled(false);
						//stopButton.setEnabled(true);
					}else{
						Toast.makeText(getActivity(), "请等待当前任务结束！", 2000).show();
					}*/
					/*String s=appInfoList.get(position).getSOFT_VERSION();
					int n=packageInfo.versionCode;
					int version=appInfoList.get(position).getVERSIONCODE();
					if(n<version){*/
                        ProgressDialog pd = new ProgressDialog(getActivity());
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        //pd.setMessage("有新的版本 " + uploadInfo.getVersionNo() + "，正在下载更新");
                        pd.setCancelable(false);
                        pd.show();

                        ApkUpdater updater = new ApkUpdater(pd, appInfoList.get(position), getActivity(), handler);
                        updater.start();
                        //}

                    }
                });
            }
            return convertView;
        }
        class Holder {
            TextView  appName;
            TextView  appRemark;
            TextView  appVersion;
            TextView  appVersionCode;
            TextView  updateTime;
            Button    collectionApp;
            Button    deleteApp;
            Button    updateApp;
            ImageView appSrc;
        }
    }

    /*
     * 由于用户的输入事件(点击button, 触摸屏幕....)是由主线程负责处理的，如果主线程处于工作状态，
     * 此时用户产生的输入事件如果没能在5秒内得到处理，系统就会报“应用无响应”错误。
     * 所以在主线程里不能执行一件比较耗时的工作，否则会因主线程阻塞而无法处理用户的输入事件，
     * 导致“应用无响应”错误的出现。耗时的工作应该在子线程里执行。
     */
    private DownloadTask task;

    private void exit() {
        if (task != null)
            task.exit();
    }

    private void download(String path, File savDir) {
        task = new DownloadTask(path, savDir);
        new Thread(task).start();
    }

    /**
     * UI控件画面的重绘(更新)是由主线程负责处理的，如果在子线程中更新UI控件的值，更新后的值不会重绘到屏幕上
     * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
     */
    private final class DownloadTask implements Runnable {
        private String         path;
        private File           saveDir;
        private FileDownloader loader;

        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null)
                loader.exit();
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            @Override
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = PROCESSING;
                msg.getData().putInt("size", size);
                handler.sendMessage(msg);
            }
        };

        public void run() {
            try {
                // 实例化一个文件下载器
                loader = new FileDownloader(getActivity(), path,
                        saveDir, 3);
                // 设置进度条最大值
                progressBar.setMax(loader.getFileSize());
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(FAILURE)); // 发送一条空消息对象
            }
        }
    }

    private void moveTo(int position, String userId) {
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(appInfoList.get(position).getPACKAGE_NAME());
        String type   = appInfoList.get(position).getICON_FLAG();//标识是哪个子APP
        if (intent == null) {
            ToastUtil.showLongToast(getActivity(), "未安装此app");
        } else {
            Bundle bundle = new Bundle();
            //区分进入哪个系统需要的参数，已系统包名作为唯一标识，对应规则添加即可
            List<RelationInfo> relationInfos = ConstantValue.relationInfos;
            if ("1".equals(type) && relationInfos.size() > 0) {//福卡e助手参数
                for (RelationInfo relationInfo : ConstantValue.relationInfos) {
                    if (relationInfo.getSystemName().equals("com.dysen.opencard")) {
                        intent.putExtra("tellerId", relationInfo.getTellId());//柜员号
                        intent.putExtra("orgId", relationInfo.getBrCode());//机构号
                    }
                }
            } else if ("2".equals(type) && relationInfos.size() > 0) {//信用卡进件，客户经理
                for (RelationInfo relationInfo : ConstantValue.relationInfos) {
                    if (relationInfo.getSystemName().equals("com.pactera.financialmanager")) {
                    }
                }
            }
            intent.putExtras(bundle);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int Id = v.getId();
        switch (Id) {
            case R.id.progressBar:
                if (whetherDownload == 0) {
                    Toast.makeText(getActivity(), "继续下载!!", Toast.LENGTH_LONG).show();
                    whetherDownload = 1;
                    String path     = "http://abv.cn/music/光辉岁月.mp3";
                    String filename = path.substring(path.lastIndexOf('/') + 1);

                    try {
                        // URL编码（这里是为了将中文进行URL编码）
                        filename = URLEncoder.encode(filename, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    path = path.substring(0, path.lastIndexOf("/") + 1) + filename;
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        // File savDir =
                        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                        // 保存路径
                        File savDir = Environment.getExternalStorageDirectory();
                        download(path, savDir);
                        //pre.putInt("downloadPosition", position);
                    } else {
                        Toast.makeText(getActivity(),
                                R.string.sdcarderror, Toast.LENGTH_LONG).show();
                    }
                } else {
                    exit();
                    Toast.makeText(getActivity(), "停止下载!!", Toast.LENGTH_LONG).show();
                    whetherDownload = 0;
                }


                break;

            default:
                break;
        }
    }

    private void getApp() {
        // TODO Auto-generated method stub
        pageManager = getActivity().getPackageManager();
        packages = pageManager.getInstalledPackages(0);
        // 获得包名
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.contains("gatewaytest")) {
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appRemark = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                info.appVersion = packageInfo.versionCode + "";
                info.updateTime = packageInfo.firstInstallTime + "";
                info.packageName = packageInfo.packageName;
                info.activityName = packageInfo.packageName + ".MainActivity";
                info.iconFlag = "1";
                //appInfoList.add(info);
            }
        }
    }

}
