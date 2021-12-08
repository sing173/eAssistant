package com.examples.gatewaydemo.model;

import java.io.Serializable;

/**
 * Created by hutian on 2018/7/12.
 * 主APP与子APP通过CRM账号绑定映射
 */

public class RelationInfo  implements Serializable {
    private  String tellId;//柜员号
	private String systemName;//系统名
	private String brCode;//机构号

	public String getTellId() {
		return tellId;
	}

	public RelationInfo setTellId(String tellId) {
		this.tellId = tellId;
		return this;
	}

	public String getSystemName() {
		return systemName;
	}

	public RelationInfo setSystemName(String systemName) {
		this.systemName = systemName;
		return this;
	}

	public String getBrCode() {
		return brCode;
	}

	public RelationInfo setBrCode(String brCode) {
		this.brCode = brCode;
		return this;
	}

	@Override
	public String toString() {
		return "RelationInfo{}";
	}
}
