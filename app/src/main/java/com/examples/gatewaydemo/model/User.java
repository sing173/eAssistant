package com.examples.gatewaydemo.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.examples.gatewaydemo.util.GetDeviceSerialNumberUtil;
import com.examples.gatewaydemo.util.Tool;

/**
 * 登录用户，即管理员
 * 
 * @author Administrator
 * 
 */
public class User {

	//这是接口文档中的返回的字段
	private String rightStr;//权限
	private String brID; // 客户所在银行ID，机构号
	private String brName; // 客户所在银行名字，机构名称
	private String userID;//用户ID (这个字段接口文档上写的是可有可无的)
	private String userCode;//用户号
	private String tyBrID;//体验银行机构号
	private String eixtTime = "";//无操作退出时间
	private String brCode;//机构号
	private String staId;//岗位ID
	private String CERTCODE;//用户身份证号 为了传值给信用卡特意添加的字段。
	private String roleCode;
	private String roleDesc;
	private String roleName;

	private String USERVALIDSTATUS;
	private String ENABLEFLAG;

	private String userid; //将userid的值赋为userCode.  这是因为有些接口我现在用的userid,
						   //但他们后台改了要用usercode返回的值
	
	private String userid1; // 登陆帐号      //目前这个版本，登录成功之后保存了登录的帐号和密码。
	private String password; // 密码
	
	
	//这是以后可能会用到的字段
	private String imei;
	private String macadd; // MAC_ADDESS
	private String versionName; // 版本名
	
	//这是这个项目之前就存在的字段，以后若是没有用处的话，就直接删除
	private String username; // 用户名称
	private int gender; // 性别
	private String brName2; // 设备所在银行名字
	private String brID2; // 设备所在银行ID

	// 用户所在法人清算中心
	private String clearingCenterBrCode;
	/*public User(Context context) throws Exception {
		// 设备序列号
		this.imei = GetDeviceSerialNumberUtil.getAndroidId(context);
		if(TextUtils.isEmpty(imei)){
			// androidID
			this.imei = GetDeviceSerialNumberUtil.getSerialNumber();
		}

		ParentActivity.ROOT_DIR = ParentActivity.TMP_ROOT_DIR + "_"+ Tool.ecodeByMD5(this.imei);
		WifiManager wifiMan = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiMan != null) {
			WifiInfo info = wifiMan.getConnectionInfo();
			this.macadd = info.getMacAddress();
		}

		PackageManager pm = context.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		this.versionName = pi.versionName;
		if (versionName == null || "".equals(versionName)) {
			versionName = VersionUtils.getVersionName(context);
		}

	}*/
	public String getRightStr() {
		return rightStr;
	}

	public void setRightStr(String rightStr) {
		this.rightStr = rightStr;
	}

	public String getBrID() {
		return brID;
	}

	public void setBrID(String brID) {
		this.brID = brID;
	}

	public String getBrName() {
		return brName;
	}

	public void setBrName(String brName) {
		this.brName = brName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getTyBrID() {
		return tyBrID;
	}

	public void setTyBrID(String tyBrID) {
		this.tyBrID = tyBrID;
	}

	public String getEixtTime() {
		return eixtTime;
	}

	public void setEixtTime(String eixtTime) {
		this.eixtTime = eixtTime;
	}

	public String getBrCode() {
		return brCode;
	}

	public void setBrCode(String brCode) {
		this.brCode = brCode;
	}

	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getCERTCODE() {
		return CERTCODE;
	}

	public void setCERTCODE(String CERTCODE) {
		this.CERTCODE = CERTCODE;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUSERVALIDSTATUS() {
		return USERVALIDSTATUS;
	}

	public void setUSERVALIDSTATUS(String USERVALIDSTATUS) {
		this.USERVALIDSTATUS = USERVALIDSTATUS;
	}

	public String getENABLEFLAG() {
		return ENABLEFLAG;
	}

	public void setENABLEFLAG(String ENABLEFLAG) {
		this.ENABLEFLAG = ENABLEFLAG;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid1() {
		return userid1;
	}

	public void setUserid1(String userid1) {
		this.userid1 = userid1;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMacadd() {
		return macadd;
	}

	public void setMacadd(String macadd) {
		this.macadd = macadd;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBrName2() {
		return brName2;
	}

	public void setBrName2(String brName2) {
		this.brName2 = brName2;
	}

	public String getBrID2() {
		return brID2;
	}

	public void setBrID2(String brID2) {
		this.brID2 = brID2;
	}

	public String getClearingCenterBrCode() {
		return clearingCenterBrCode;
	}

	public void setClearingCenterBrCode(String clearingCenterBrCode) {
		this.clearingCenterBrCode = clearingCenterBrCode;
	}

	@Override
	public String toString() {
		return "User{" +
				"rightStr='" + rightStr + '\'' +
				", brID='" + brID + '\'' +
				", brName='" + brName + '\'' +
				", userID='" + userID + '\'' +
				", userCode='" + userCode + '\'' +
				", tyBrID='" + tyBrID + '\'' +
				", eixtTime='" + eixtTime + '\'' +
				", brCode='" + brCode + '\'' +
				", staId='" + staId + '\'' +
				", CERTCODE='" + CERTCODE + '\'' +
				", roleCode='" + roleCode + '\'' +
				", roleDesc='" + roleDesc + '\'' +
				", roleName='" + roleName + '\'' +
				", USERVALIDSTATUS='" + USERVALIDSTATUS + '\'' +
				", ENABLEFLAG='" + ENABLEFLAG + '\'' +
				", userid='" + userid + '\'' +
				", userid1='" + userid1 + '\'' +
				", password='" + password + '\'' +
				", imei='" + imei + '\'' +
				", macadd='" + macadd + '\'' +
				", versionName='" + versionName + '\'' +
				", username='" + username + '\'' +
				", gender=" + gender +
				", brName2='" + brName2 + '\'' +
				", brID2='" + brID2 + '\'' +
				", clearingCenterBrCode='" + clearingCenterBrCode + '\'' +
				'}';
	}
}
