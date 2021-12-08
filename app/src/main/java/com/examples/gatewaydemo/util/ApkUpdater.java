package com.examples.gatewaydemo.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.examples.gatewaydemo.model.UploadInfo;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class ApkUpdater {
	private ProgressDialog pd;

    private UploadInfo uploadInfo;

    private Activity activity;

    private Handler handler;

    private   String BASE_PATH;//文件路径

    public ApkUpdater(ProgressDialog pd, UploadInfo uploadInfo, Activity activity,Handler handler) {
        this.uploadInfo = uploadInfo;
        this.activity = activity;
        this.pd = pd;
        this.handler=handler;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                //final String BASE_PATH = "/fkezs";
                final String fileName = "tmp-" + uploadInfo.getSOFT_VERSION() + ".tmp";
                 BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fkezs";
                //File logFileDir = new File(logDirectory);
                File dir = new File( BASE_PATH); //
                if (!dir.exists()) {

                    try {
                    	
                    	//if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    		boolean mkdirs =  dir.mkdirs();
                              dir.mkdirs();
                            Toast.makeText(activity,mkdirs+"path"+dir.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                    	//}
                        if (!dir.isDirectory())
                        {
                            throw new IllegalArgumentException("The logcat folder path is not a directory: " + dir);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
                // delete other temp files, and old apk files
                {
                    File[] delFiles = dir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.startsWith("tmp-") && filename.endsWith(".tmp") && !filename.equals(fileName)) {
                                return true;
                            }
                            if (filename.endsWith(".apk")) {
                                return true;
                            }
                            return false;
                        }
                    });
                    if (delFiles != null) {
                        for (File f : delFiles) {
                            f.deleteOnExit();
                        }
                    }
                }

                File tmpFile = new File(dir, fileName);
                while(true) {

                    long rangeFrom = 0;
                    if (tmpFile.exists()) {
                        rangeFrom = tmpFile.length();
                    }

                    HttpClient hc = new DefaultHttpClient();
                    hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30 * 1000);
                    hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
                   // HttpGet get = new HttpGet(uploadInfo.getSOFT_PATH().replace("http://172.48.12.9:7081", "http://27.17.37.104:9995"));
                   LogUtils.d("hut","下载更新路径："+uploadInfo.getSOFT_PATH());
                    HttpGet get = new HttpGet(uploadInfo.getSOFT_PATH());
					get.setHeader("Range", rangeFrom + "-");

                    BufferedInputStream bis = null;
                    FileOutputStream fos = null;
                    try {
                        HttpResponse response = hc.execute(get);
                        if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {



                            int totalLength = getTotalLength(response);
                            pd.setMax(totalLength);
                            pd.setProgress((int)rangeFrom);

                            HttpEntity entity = response.getEntity();

                            bis = new BufferedInputStream(entity.getContent());
                            fos = new FileOutputStream(tmpFile, true);

                            byte[] buffer = new byte[1024 * 4];
                            int rd;
                            int written = (int)rangeFrom;
                            while ((rd = bis.read(buffer)) != -1) {
                                fos.write(buffer, 0, rd);
                                written += rd;
                                //��ȡ��ǰ������
                                pd.setProgress(written);
                            }

                            break;
                        }
                    } catch (IOException e) {
                        Message msg= Message.obtain();
                        msg.what=-1;
                        msg.obj="下载网络异常";
                        handler.sendMessage(msg);
                        if(pd!=null&&pd.isShowing()){
                            pd.dismiss();
                        }
                        e.printStackTrace();
                        continue;
                    } finally {
                        try {
                            bis.close();
                        } catch (Exception e) {
                        }
                        try {
                            fos.close();
                        } catch (Exception e) {
                        }
                    }
                }

                File renamed = new File(tmpFile.getParentFile(), uploadInfo.getSOFT_VERSION() + ".apk");
                tmpFile.renameTo(renamed);

                pd.dismiss(); //�������������Ի���

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                	intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                	Uri contentUri = null;
					try {
						contentUri = FileProvider.getUriForFile(activity.getApplicationContext(),"com.examples.gatewaydemo.fileprovider", renamed);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                }else{
                	intent.setDataAndType(Uri.fromFile(renamed), "application/vnd.android.package-archive");
                	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }        
                try {
					activity.startActivity(intent);
					//activity.finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }.start();
    }


    private int getTotalLength(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        int totalLength = (int) entity.getContentLength();
        if (totalLength != -1) {
            return totalLength;
        }

        Header[] headers = response.getHeaders("Content-Range");
        if (headers == null || headers.length == 0) {
            return -1;
        }

        String v = headers[0].getValue();
        String[] sp = v.split(" ");
        if (sp.length < 2) {
            return -1;
        }

        v = sp[1];
        if (v.contains("/")) {
            return Integer.valueOf(v.substring(v.indexOf("/") + 1));
        }

        sp = v.split("-");
        if (sp.length != 2) {
            return -1;
        }
        return Integer.valueOf(sp[1]) + 1;
    }
}
