package com.ledboot.wegirls.utils;

public class Config {
	
	//发布版本
	public static final int VERSION_DEV_LOCAL =  2;  //内网开发版本
	
	public static final int VERSION_DEV_INET = 4 ;   //外网开发版本
	
	public static final int VERSION_TEST_LOCAL = 8 ;  //内网测试版本
	
	public static final int VERSION_TEST_INET = 16 ;  //外网测试版本
	
	//警告：正式版是运营版本，涉及数据安全和服务稳定性，在开发和测试中不得使用该版本！！！
	public static final int VERSION_STABLE = 32 ;     //正式发布版本
	
	

	//以下是版本配置
	public static final int RELEASE_VERSION = VERSION_DEV_INET;              //服务器版本

	public static final boolean DEBUG_VERSION = true;                     //是否是Debuger版本
	  
	public static final int DEBUG_LEVEL = Debuger.LOG_LEVEL_VERBOSE ;        //Debuger等级
	
//	public static final int PUBLISH_CHANNEL = PublishChannel.OFFICIAL;     //发布的渠道
}
