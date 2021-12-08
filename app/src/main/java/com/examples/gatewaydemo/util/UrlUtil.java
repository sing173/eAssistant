package com.examples.gatewaydemo.util;

public class UrlUtil {
	public static int netType=2;
	public static String SERVICE_IP="http://172.48.12.9:7081/";///默认生产 //测试-http://192.168.2.111:7001/
	//public static String SERVICE_IP="http://192.168.2.111:7001/";//测试
	//public static String SERVICE_IP="http://27.17.37.104:9995/";//外网
	public static String getServiceIp(){
		if(netType==1){
			SERVICE_IP = "http://192.168.2.111:7001/";
		}else if(netType==2){
			SERVICE_IP = "http://172.48.12.9:7081/";
		}else if(netType==3){
			SERVICE_IP = "http://27.17.37.104:9995/";
		}
		return SERVICE_IP;
	}
	
	public static String[] netInfo={"测试：http://192.168.2.111:7001/","生产：http://172.48.12.9:7081/","外网:http://27.17.37.104:9995/"};
}
