package com.examples.gatewaydemo.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.examples.gatewaydemo.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Tool {
	public static final String TMP_ROOT_DIR = "JNBANK";// 根目录
	public static boolean FAG = false;
	private static final int REQUEST_ENABLE_BT = 2;
	public static float lockTime = 10000;// 30分钟
	// 小 中 大 图片的本地存储路径
	// 小 中 大 图片的本地存储路径
	public static final String path_minpic = Tool.TMP_ROOT_DIR + "minPic";
	public static final String path_midpic = Tool.TMP_ROOT_DIR + "midPic";
	public static final String path_largepic = Tool.TMP_ROOT_DIR + "largePic";
	public static final String path_file = Tool.TMP_ROOT_DIR + "file";
	public static final String path_map = Tool.TMP_ROOT_DIR+"map";
	
	public static boolean isFAG() {
		return FAG;
	}

	public static void setFAG(boolean fAG) {
		FAG = fAG;
	}

	public static final String paht_guijinshu = Tool.TMP_ROOT_DIR+"guijinshu";
	public static final String USERID = "userid";
	public static final String PASS = "pass";
	public static final String GENDER = "gender";
	public static final int BITMAP_ZOOM_RATE = 300;
	final static int BUFFER_SIZE = 1024;
	@SuppressWarnings("unused")
	private final static String regxpForHtml = "(?<=<(p)>).*(?=<\\/\\1>)"; // 过滤所有以“<”开头，以“>”结尾的标签
	@SuppressWarnings("unused")
	private final static String regxpZz = "ƵƵ([.]*)ƶƶ";

	private Tool() {
	}

	/** 判断有无sd */
	public static boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/** 以最省内存的方式读取本地资源的图片 */
	public static Bitmap readBitMap(Context context, int resId) {
		InputStream is = context.getResources().openRawResource(resId);
		return readBitMap(is);
	}

	/**
	 * InputStream转化成BitMap
	 */
	public static Bitmap readBitMap(InputStream is) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	public static Bitmap readBitMap(InputStream is, int Size) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inSampleSize = Size;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	/** 获取URL文件名 */
	public static String getURLFileName(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		// fileName = URLDecoder.decode(fileName);
		return fileName;
	}

	/** 获取已经下载的该文件 */
	public static File getURLFile(String filePath, String fileName) {
		StringBuilder sbDir = new StringBuilder();
		sbDir.append(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		sbDir.append(File.separator);
		sbDir.append(TMP_ROOT_DIR);
		sbDir.append(File.separator);
		sbDir.append(filePath);
		sbDir.append(File.separator);
		String directory = sbDir.toString();
		return new File(directory, fileName);
	}

	/** 判断是否已经下载该文件 */
	public static boolean haveURLFile(String filePath, String fileName) {
		return getURLFile(filePath, fileName).exists();
	}

	/**
	 * 
	 * 在手机内存创建根目录
	 */
	public static boolean creatRootDir() {
		String sDStateString = android.os.Environment.getExternalStorageState();
		if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			StringBuilder sbDir = new StringBuilder();
			sbDir.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			sbDir.append(File.separator);
			sbDir.append(TMP_ROOT_DIR);
			sbDir.append(File.separator);
			String directory = sbDir.toString();
			File dirFile = new File(directory);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			return true;
		} else {
			return false;
		}
	}

	/** 网络数据写入sd */
	public static boolean writeURLFile2SDCard(String filePath, String fileName,
			InputStream input) {
		String sDStateString = android.os.Environment.getExternalStorageState();
		if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			StringBuilder sbDir = new StringBuilder();
			sbDir.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			sbDir.append(File.separator);
			sbDir.append(TMP_ROOT_DIR);
			sbDir.append(File.separator);
			sbDir.append(filePath);
			String directory = sbDir.toString();
			File dirFile = new File(directory);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File file = new File(dirFile, fileName);
			if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				byte[] b = new byte[BUFFER_SIZE];
				int j = 0;
				try {
					while ((j = input.read(b)) != -1) {
						fos.write(b, 0, j);
					}
					fos.flush();
					fos.close();
					input.close();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					if (file.exists())
						file.delete();
					return false;
				}
			} catch (FileNotFoundException e) {
				Log.e("shibai", "shibai");
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else
			return false;
		return false;
	}

	
	/** 网络数据写入sd连接hanlder */
	public static boolean writeURLFile2SDCard(String filePath, String fileName,
			InputStream input,Handler handler,int length) {
		String sDStateString = android.os.Environment.getExternalStorageState();
		if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			StringBuilder sbDir = new StringBuilder();
			sbDir.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			sbDir.append(File.separator);
			sbDir.append(TMP_ROOT_DIR);
			sbDir.append(File.separator);
			sbDir.append(filePath);
			String directory = sbDir.toString();
			File dirFile = new File(directory);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File file = new File(dirFile, fileName);
			if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				byte[] b = new byte[BUFFER_SIZE*4];
				int j = 0;
				int total = 0;
				double rate = (double) 100 / length;
				try {
					long time = System.currentTimeMillis();
					while ((j = input.read(b)) != -1) {
						fos.write(b, 0, j);
						total += j;
						int p = (int)(total*rate);
						
						if(System.currentTimeMillis() - time > 3000){
							Log.e("gg", "!!!");
							time = System.currentTimeMillis();
							Message message = handler.obtainMessage();
							message.what = -3;
							message.arg1 = p;
							handler.sendMessage(message);
						}
						
					}
					fos.flush();
					fos.close();
					input.close();
					if (total == length) {
						handler.sendEmptyMessage(-2);
					}
					return true;
				} catch (IOException e) {
					handler.sendEmptyMessage(-1);
					e.printStackTrace();
					if (file.exists())
						file.delete();
					return false;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else
			return false;
		return false;
	}

	
	
	/**
	 * 根据文件的URL向SDCard写入数据
	 */
	public static boolean writeURLFile2SDCard(String filePath, String fileName,
			InputStream input, int fileSize) {
		if (writeURLFile2SDCard(filePath, fileName, input)) {
			File file = getURLFile(filePath, fileName);
			if ((int) file.length() == fileSize) {
				Log.i("---", "fileLength=" + file.length());
				return true;
			} else {
				if (file.exists())
					file.delete();
				return false;
			}
		}
		return false;
	}

	/** SD卡读取File到InputStream */
	public static InputStream readURLFileFromSDCard(String filePath,
			String fileName) throws FileNotFoundException {
		File file = getSDFile(filePath, fileName);
		InputStream is = null;
		if (file.exists())
			is = new FileInputStream(file);
		return is;
	}

	/** 获取SD卡上的File */
	public static File getSDFile(String path, String fileName) {
		StringBuilder sbDir = new StringBuilder();
		sbDir.append(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		sbDir.append(File.separator);
		sbDir.append(TMP_ROOT_DIR);
		sbDir.append(File.separator);
		sbDir.append(path);
		sbDir.append(File.separator);

		return new File(sbDir.toString(), fileName);
	}

	/** 删除SD卡上的File */
	public static void delSDFile(String path, String fileName) {
		Log.i("---", "---deletePath---"+path);
		File file = getSDFile(path, fileName);
		if (file.exists()) {
			if (file.isFile())
				file.delete();
		}else{
			Log.i("---","---deletePath---"+"文件不存在！");
		}
	}

	/** 获取PDF文件的intent */
	public static Intent getPDFFileIntent(File file) {
		Uri uri = Uri.fromFile(file);
		String type = "application/pdf";
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, type);
		return intent;
	}

	/**
	 * @param 两点的经纬度
	 * @return 两点间距离，单位:米
	 * */
	public static double distance(double lat1, double lon1, double lat2,
			double lon2) {
		double jl_jd = 102834.74258026089786013677476285;
		double jl_wd = 111712.69150641055729984301412873;
		double b = Math.abs((lon1 - lon2) * jl_jd);
		double a = Math.abs((lat1 - lat2) * jl_wd);
		return Math.sqrt((a * a + b * b));
	}

	/**
	 * 过滤掉字符串中的非法字符（还是unicode字符，但由于private区间所以无法显示的字符）
	 * 
	 * @param s
	 * @return
	 */
	public static String filterIllegalChar(String s) {
		if (s == null || s.length() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
			if (ub == Character.UnicodeBlock.PRIVATE_USE_AREA) {
				continue;
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 判断字符串的编码
	 */
	@SuppressWarnings("unused")
	public static boolean getEncoding(String str) {
		boolean state = true;
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				state = false;
			}
		} catch (Exception exception) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				state = false;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				state = false;
			}
		} catch (Exception exception3) {
		}
		return state;
	}

	/**
	 * 转GBK
	 * 
	 * @param s
	 * @return
	 */
	public static String unicodeToGBK2(String s) {
		if (s == null)
			return "";
		if (s.contains("<p>"))
			s.replaceAll("<p>", "");
		if (s.contains("</p>"))
			s.replace("</p>", "");
		String[] k = s.split(";");
		if (k == null)
			return "";
		StringBuilder rs = new StringBuilder();
		for (int i = 0; i < k.length; i++) {
			int strIndex = k[i].indexOf("&#");
			String newstr = k[i];
			if (strIndex > -1) {
				String kstr = "";
				if (strIndex > 0) {
					kstr = newstr.substring(0, strIndex);
					rs.append(kstr);
					newstr = newstr.substring(strIndex);
				}
				int m = Integer.parseInt(newstr.replace("&#", ""));
				char c = (char) m;
				rs.append(c);
			} else {
				rs.append(newstr);
			}
		}
		return rs.toString();
	}

	// 验证邮箱
	public static boolean checkEmail(String email) {
		if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
			return true;
		} else {
			return false;
		}
	}

	// 判断蓝牙是否 开启
	public static boolean blueToothIsOpen(Activity activity) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		if (bluetoothAdapter == null) {
			Toast.makeText(activity, "设备不支持蓝设备!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			if (!bluetoothAdapter.isEnabled()) {
				Intent enableintent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enableintent, REQUEST_ENABLE_BT);
				Toast.makeText(activity, "请开启蓝牙", Toast.LENGTH_LONG).show();
				return false;
			} else {
				return true;
			}
		}

	}

	// 防止按钮点击两次的方法
	private static long lastCilickTime;

	public static boolean isFastDouleClick() {
		long time = System.currentTimeMillis();
		if (time - lastCilickTime < 1000) {
			return false;
		}
		lastCilickTime = time;
		return true;
	}
	public static boolean isFastScrollDouleClick() {
		long time = System.currentTimeMillis();
		if (time - lastCilickTime < 1000) {
			return false;
		}
		lastCilickTime = time;
		return true;
	}
	// 读取文本
	public static String readTxtFile(Context mContext, int fileId) {
		Resources res = mContext.getResources();
		InputStream in = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		sb.append("");
		try {
			in = res.openRawResource(fileId);
			String str;
			br = new BufferedReader(new InputStreamReader(in, "GBK"));
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}

		} catch (NotFoundException e) {
			Toast.makeText(mContext, "文本文件不存", 100).show();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, "文本编码出现异常", 100).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(mContext, "文件读取错误", 100).show();
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return sb.toString();
	}

	// 规范换行
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static String getTwoDecimal(double number) {
		DecimalFormat format = new DecimalFormat("0.00");
		String numberStr = format.format(number);
		return numberStr;
	}

	/**
	 * 对网络连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}

	// 计算屏幕大小方面
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static boolean isMobileNO(String mobiles) {
		String regExp = "^[1]([3][0-9]{1}|59|53|51|52|54|56|57|58|88|89|86|87|85|84|83|81|82|77|76|74|75|73|72|71|78|79)[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobiles);
		return m.find();
	}

	public static boolean hasSpecialCharacter(String str) {
		String regEx = "[~!@#$%^&*<>-]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 去掉从BMC中返回的头部和尾部字符串
	 * 
	 * @author 金志君
	 * @param 从BMC获取的流变成的String
	 * @return 处理过的数据
	 */
	public static String formatBmcXml(String s) {
		String rep1 = "<ns:getDataResponse xmlns:ns=";
		String rep5 = "\"" + "http://bmc.webservice.glorycube.com" + "\"" + ">";
		String rep2 = "<ns:return>";
		String rep3 = "</ns:return>";
		String rep4 = "</ns:getDataResponse>";
		String newInput = s.replaceAll(rep1, "");
		newInput = newInput.replaceAll(rep2, "");
		newInput = newInput.replaceAll(rep3, "");
		newInput = newInput.replaceAll(rep4, "");
		newInput = newInput.replaceAll(rep5, "");
		newInput = newInput.replaceAll("&lt;", "<");
		newInput = newInput.replaceAll("&gt;", ">");
		newInput = newInput.replaceAll("&#xd;", "");
		return newInput;
	}

	/**
	 * 
	 * 将经过BASE64编码的String类型文件编程List类型的文件
	 * 
	 * @author 金志君
	 * @param SceneListString
	 *            经过编码的数据
	 * @return List型数据
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List String2SceneList(String SceneListString)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
				Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				mobileBytes);
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		List SceneList = (List) objectInputStream.readObject();
		objectInputStream.close();
		return SceneList;
	}

	/**
	 * 
	 * 这是将LIST类型的数据编码成String类型的数据
	 * 
	 * @author 金志君
	 * @param SceneList
	 *            需要BASE64编码的数据
	 * @return
	 * @throws IOException
	 */
	public static String SceneList2String(List SceneList) throws IOException {
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
		objectOutputStream.writeObject(SceneList);
		// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
		String SceneListString = new String(Base64.encode(
				byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		// 关闭objectOutputStream
		objectOutputStream.close();
		return SceneListString;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * 显示Toast
	 */
	public static void ShowToast(Context context, String Text) {
		ShowToast(context, Text, 2000, 0, 0);
	}
	
	public static void ShowToastLong(Context context, String Text) {
		ShowToast(context, Text, 10000, 0, 0);
	}

	/**
	 * 显示Toast
	 */
	public static void ShowToast(Context context, String Text, int duringTime,
			int x, int y) {
		Toast toast = Toast.makeText(context, Text, duringTime);
		toast.setGravity(Gravity.CENTER, x, y);
		toast.show();
	}
	
	public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }

	/**
	 * 防止连点操作
	 * 
	 * @return
	 */
	public static boolean isFastDouleClick(long lastCilickTime) {
		long time = System.currentTimeMillis();
		if (time - lastCilickTime < 1000) {
			return false;
		}
		return true;
	}
  
  /**
   * 根据BYTE型数据写文件
   * 
   * @param filePath
   * @param fileName
   * @param input
   * @return
   */
  public static boolean write2SDCard(String filePath, String fileName,
      byte[] input) {
    String sDStateString = android.os.Environment.getExternalStorageState();
    if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
      StringBuilder sbDir = new StringBuilder();
      sbDir.append(Environment.getExternalStorageDirectory()
          .getAbsolutePath());
      sbDir.append(File.separator);
      sbDir.append(TMP_ROOT_DIR);
      sbDir.append(File.separator);
      sbDir.append(filePath);
      String directory = sbDir.toString();
      File dirFile = new File(directory);
      if (!dirFile.exists()) {
        dirFile.mkdirs();
      }
      File file = new File(dirFile, fileName);
      if (!file.exists())
        try {
          file.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(file);
        fos.write(input);
        fos.flush();
        fos.close();
        return true;

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } else
      return false;
    return false;
  }
  
  /**判断字符串是否是数字*/
  public static boolean isNumeric(String str){  
	  for (int i = str.length();--i>=0;){    
	   if (!Character.isDigit(str.charAt(i))){  
	    return false;  
	   }  
	  }  
	  return true;  
	} 

	/**
	 * 解压assets的zip压缩文件到指定目录
	 * 
	 * @param context上下文对象
	 * @param assetName压缩文件名
	 * @param outputDirectory输出目录
	 * @param isReWrite是否覆盖
	 * @throws IOException
	 */
	public static void unZip(Context context, String url,
			String outputDirectory, boolean isReWrite) throws IOException {
		// 创建解压目标目录
		File file = new File(outputDirectory);
		// 如果目标目录不存在，则创建
		if (!file.exists()) {
			file.mkdirs();
		}
		// 打开压缩文件
		FileInputStream inputStream = new FileInputStream(url);
		ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
//		InputStream inputStream = context.getAssets().open(url);
//		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		// 使用1Mbuffer
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
		while (zipEntry != null) {
			// 如果是一个目录
			if (zipEntry.isDirectory()) {
				file = new File(outputDirectory + File.separator
						+ zipEntry.getName());
				// 文件需要覆盖或者是文件不存在
				if (isReWrite || !file.exists()) {
					file.mkdir();
				}
			} else {
				// 如果是文件
				file = new File(outputDirectory + File.separator
						+ zipEntry.getName());
				// 文件需要覆盖或者文件不存在，则解压文件
				if (isReWrite || !file.exists()) {
					file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);
					while ((count = zipInputStream.read(buffer)) > 0) {
						fileOutputStream.write(buffer, 0, count);
					}
					fileOutputStream.close();
				}
			}
			// 定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}
	
	
	
	//获取视频缩略图
	public static Bitmap getVideoThumbnail(String filePath) {  
    Bitmap bitmap = null;  
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
    try {  
        retriever.setDataSource(filePath);  
        bitmap = retriever.getFrameAtTime();  
    }   
    catch(IllegalArgumentException e) {  
        e.printStackTrace();  
    }   
    catch (RuntimeException e) {  
        e.printStackTrace();  
    }   
    finally {  
        try {  
            retriever.release();  
        }   
        catch (RuntimeException e) {  
            e.printStackTrace();  
        }  
    }  
    return bitmap;  
}  
	
	private static double monthRate(double rate,int period){
		double rateSum=1;
		for(int i=1;i<=period;i++){
		rateSum=rateSum*rate;
		}
		return rateSum;
	}
	
	
	public static double calulate(double amount,double rate,int period ){
		double[] group=new double[period];
		int xx=period;
		for(int i=0;i<=period-1;i++){
		group[i]=amount*(monthRate(1+rate/1200, xx));
		xx--;
		}
		double sum=0;
		for(int i=0;i<=period-1;i++){
			sum+=group[i];
		}		
		return sum;
	}
	
	public static boolean checkPassword(String password,String accountNo,String certNo){
		int n1=Integer.parseInt(password.substring(0, 1));
		int n2=Integer.parseInt(password.substring(1, 2));
		int n3=Integer.parseInt(password.substring(2, 3));
		int n4=Integer.parseInt(password.substring(3, 4));
		int n5=Integer.parseInt(password.substring(4, 5));
		int n6=Integer.parseInt(password.substring(5, 6));
		if((password.substring(0, 2).equals(password.substring(2, 4))&&(password.substring(0, 2).equals(password.substring(4, 6))))){
			return false;
		}else if((password.substring(0, 3).equals(password.substring(3, 6)))){
			return false;
		}else if( (password.substring(0, 1).equals(password.substring(1, 2)))&&(password.substring(2, 3).equals(password.substring(3, 4)))&&(password.substring(4, 5).equals(password.substring(5, 6)))){
			return false;
		}else if(accountNo.contains(password)||certNo.contains(password)){
			return false;
		}else if((password.substring(0, 1).equals(password.substring(1, 2)))&&(password.substring(0, 1).equals(password.substring(2, 3)))&&(password.substring(0, 1).equals(password.substring(3, 4)))&&(password.substring(0, 1).equals(password.substring(4, 5)))&&(password.substring(0, 1).equals(password.substring(5, 6)))){
			return false;
		}else if((n2-n1)==1&&(n3-n2)==1){
			return false;
		}else if((n1==n2&&n2==n3)&&(n4==n5)&&(n5==n6)){
			return false;
		}else{
			return true;
		}
	}
	
	public static String blueToothInfoGet(Context context){
		String info="";
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = adapter.getBondedDevices();  
        if(devices.size()>0){  
            for(Iterator iterator = devices.iterator();iterator.hasNext();){  
                BluetoothDevice device = (BluetoothDevice) iterator.next();
                info=info+device.getName()+"---";
            }  
        }
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
        String DEVICE_ID = tm.getDeviceId(); 
		return info;
	}
	
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
		if (file.isFile()) { // 判断是否是文件
		file.delete(); // delete()方法 你应该知道 是删除的意思;
		} else if (file.isDirectory()) { // 否则如果它是一个目录
		File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
		for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
			deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
		}
		}
		//file.delete();
		} else {
			Log.i("---", "文件不存在！");
		}
		}
	
	public static boolean haveNet(Activity context) {
		Toast toast = Toast.makeText(context, R.string.unNetwork,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(context);
		imageCodeProject.setImageResource(R.drawable.comm_error);
		toastView.addView(imageCodeProject, 0);
		if (!havaNetNoToast(context)) {
			toast.show();
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean havaNetNoToast(Activity context) {
		try {
			ConnectivityManager cwjManager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			if (cwjManager == null) {
				return false;
			} else {
				NetworkInfo info = cwjManager.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * 
	 * 
	 * 将指定的字符串用MD5加密32位
	 * 
	 * @param originstr
	 *            需要加密的字符串
	 * 
	 * 
	 * @return 加密后的字符串
	 */

	public static String ecodeByMD5(String originstr) {

		String result = null;

		char hexDigits[] = {// 用来将字节转换成 16 进制表示的字符

		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };

		if (originstr != null) {

			try {

				// 返回实现指定摘要算法的 MessageDigest 对象

				MessageDigest md = MessageDigest.getInstance("MD5");

				// 使用utf-8编码将originstr字符串编码并保存到source字节数组

				byte[] source = originstr.getBytes("utf-8");

				// 使用指定的 byte 数组更新摘要

				md.update(source);

				// 通过执行诸如填充之类的最终操作完成哈希计算，结果是一个128位的长整数

				byte[] tmp = md.digest();

				// 用16进制数表示需要32位

				char[] str = new char[32];

				for (int i = 0, j = 0; i < 16; i++) {

					// j表示转换结果中对应的字符位置

					// 从第一个字节开始，对 MD5 的每一个字节

					// 转换成 16 进制字符

					byte b = tmp[i];

					// 取字节中高 4 位的数字转换

					// 无符号右移运算符>>> ，它总是在左边补0

					// 0x代表它后面的是十六进制的数字. f转换成十进制就是15

					str[j++] = hexDigits[b >>> 4 & 0xf];

					// 取字节中低 4 位的数字转换

					str[j++] = hexDigits[b & 0xf];

				}

				result = new String(str);// 结果转换成字符串用于返回

			} catch (NoSuchAlgorithmException e) {

				// 当请求特定的加密算法而它在该环境中不可用时抛出此异常

				e.printStackTrace();

			} catch (UnsupportedEncodingException e) {

				// 不支持字符编码异常

				e.printStackTrace();

			}

		}

		return result;

	}
	
	public static String getJsonString(InputStream inputStream, String type) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, type);

        int event = parser.getEventType();// 产生第一个事件
        String tmpJsonString = null;
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("getJSONReturn".equals(parser.getName())) {// 判断开始标签元素是否是student
                        tmpJsonString = parser.nextText();// 得到student标签的属性值，并设置student的id
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }
        return tmpJsonString;
    }
	
	public static <T> List<T> parseList(String jsonData, Class<T> cls) {

        if (!TextUtils.isEmpty(jsonData) || jsonData != null) {
            Gson gson = new Gson();
            List<T> list = new ArrayList<T>();
            JsonArray arry = new JsonParser().parse(jsonData).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
            return list ;

        } else
            return null;
    }
	
	public static String getSerialNo(Context context){
		String serialNo="";
		
		try {
			Class<?> c=Class.forName("android.os.SystemProperties");
			Method get=c.getMethod("get", String.class);
			serialNo=(String) get.invoke(c, "ro.serialno");
			if(serialNo.equals("")&&Build.VERSION.SDK_INT>25){
				serialNo=Build.SERIAL;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return serialNo;
	}
	
	public static String getIMEI(Context context) {
//      return ((TelephonyManager) context
//              .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();//50F0D3A9B6F1
  	String seriNo = ((TelephonyManager) context
              .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();    
  	if(TextUtils.isEmpty(seriNo)||("000000000000000").equals(seriNo)||("00000000").equals(seriNo)||("020000000000").equals(seriNo)){
  		seriNo = getBluetoothAddress(context);
  	}
  	return seriNo;
  }
	
	public static String getBluetoothAddress(Context context){
    	// androidID
		String androidID = android.provider.Settings.Secure.getString(context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		// 返回wifi MAC地址
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()) {
			wifi.setWifiEnabled(true);
		}
//		String wifiAddress = wifi.getConnectionInfo().getMacAddress();会得到虚拟的mac地址0200000000000
        String wifiAddress = "";
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
            wifiAddress = convertMacAddressBytesToString(networkInterface.getHardwareAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

		if(!TextUtils.isEmpty(wifiAddress)){
            androidID = wifiAddress.replaceAll(":", "");
		}
		return androidID;
    }
	
	private static String convertMacAddressBytesToString(byte[] addres){
        StringBuffer sb = new StringBuffer();
        if (addres != null && addres.length > 1) {
            sb.append(parseByte(addres[0])).append(":").append(
                    parseByte(addres[1])).append(":").append(
                    parseByte(addres[2])).append(":").append(
                    parseByte(addres[3])).append(":").append(
                    parseByte(addres[4])).append(":").append(
                    parseByte(addres[5]));
            return sb.toString();
        }
        return "00:00:00:00:00";
    }
	
	private static String parseByte(byte b) {
        int intValue = 0;
        if (b >= 0) {
            intValue = b;
        } else {
            intValue = 256 + b;
        }
        return Integer.toHexString(intValue);
    }

    private static PackageInfo getPackageInfo(Context context){
		PackageInfo pi=null;
		try {
			PackageManager pm=context.getPackageManager();
			pi=pm.getPackageInfo(context.getPackageName(),PackageManager.GET_CONFIGURATIONS);
			return pi;
		}catch (Exception e){
			e.printStackTrace();
		}
		return pi;
	}

	public static String getVersionName(Context context){
    	return getPackageInfo(context).versionName;
	}

	//根据日期清除超过七天的无用日志
	public static List<String> deleteFileByDate(String path){
		File file=new File(path);
		File[] files=file.listFiles();
		if(files == null){
			return null;
		}
		List<String> s=new ArrayList<>();
		for(int i=0;i<files.length;i++){
			String temp=files[i].getAbsolutePath();
			int flag=0;
			ArrayList <String> arrayList= DateFormatUtil.getWeekData2();
			if(arrayList!=null&&arrayList.size()>=0){
				for(int j=0;j<arrayList.size();j++){
					String tempDate=arrayList.get(j);
					if(temp.contains(tempDate)){
						flag++;
						break;
					}else{
						flag=0;
					}
				}
			}
			if(flag>=1){

			}else{
				deleteFile(files[i]);
			}
		}
		return s;
	}
}