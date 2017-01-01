package cn.kcn.user.web.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.struts2.ServletActionContext;

import cn.kcn.user.utils.DownloadUtils;

import com.opensymphony.xwork2.ActionSupport;
//获取请求参数  属性驱动   第一种  直接将action做为model
@SuppressWarnings("serial")
public class DownloadAction extends ActionSupport {
	
	private String filename;//要下载文件的名称
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	//设置下载文件mimeType类型
	public String getContentType() {
		
		String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
		
		return mimeType;
	}
	
	public InputStream getInputStream() throws FileNotFoundException, UnsupportedEncodingException {
		//解决中文名乱码
		filename = new String(filename.getBytes("iso8859-1"),"utf-8");
		FileInputStream fis = new FileInputStream("C:/Users/HeZhanfeng/Desktop/Struts2/"+filename);
		return fis;
	}
	//获取下载文件名称
	public String getDownloadFileName() throws UnsupportedEncodingException{
		//调用下载工具类
		return DownloadUtils.getDownloadFileName(ServletActionContext.getRequest().getHeader("user-agent"), filename);
	}
	
	@Override
	public String execute() throws Exception {
	   
		System.out.println("进行下载。。。");
		
		return SUCCESS;
	}
}