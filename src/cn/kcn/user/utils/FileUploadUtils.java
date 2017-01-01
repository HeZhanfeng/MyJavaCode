package cn.kcn.user.utils;

import java.util.UUID;

public class FileUploadUtils {
	
	//得到随机名
	public static String getUUIDFileName(String filename){
		int index = filename.lastIndexOf(".");
		
		return UUID.randomUUID()+filename.substring(index);
	}
}
