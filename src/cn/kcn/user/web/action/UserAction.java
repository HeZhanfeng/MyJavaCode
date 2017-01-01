package cn.kcn.user.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Encoder;
import cn.kcn.user.damain.User;
import cn.kcn.user.service.UserService;
import cn.kcn.user.utils.FileUploadUtils;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

public class UserAction extends ActionSupport implements ModelDriven<User>{
	private List<User> users;
	private User user = new User();
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getModel() {
		return user;
	}
	
	
	/**
	 * 下载简历 (struts2 下载 一个流、 两个头信息)
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public String download() throws SQLException {
		UserService userService = new UserService();
		user = userService.findById(user.getUserID());

		return "download_success";
	}

	// 返回文件流
	public InputStream getInputStream() throws IOException {
		if (user == null || user.getPath() == null) {
			return null;
		}
		File file = new File(ServletActionContext.getServletContext()
				.getRealPath(user.getPath()));
		System.out.println("====:"+file);
		return new FileInputStream(file);
	}

	// 返回简历 MIME类型
	public String getContentType() {
		if (user == null || user.getFilename() == null) {
			return null;
		}
		return ServletActionContext.getServletContext().getMimeType(
				user.getFilename());
	}

	// 返回编码后的文件名
	public String getDownloadFilename() throws IOException {
		if (user == null || user.getFilename() == null) {
			return null;
		}
		return encodeDownloadFilename(user.getFilename(), ServletActionContext
				.getRequest().getHeader("user-agent"));
	}

	/**
	 * 下载文件时，针对不同浏览器，进行附件名的编码
	 * 
	 * @param filename
	 *            下载文件名
	 * @param agent
	 *            客户端浏览器
	 * @return 编码后的下载附件名
	 * @throws IOException
	 */
	public String encodeDownloadFilename(String filename, String agent)
			throws IOException {
		if (agent.contains("Firefox")) { // 火狐浏览器
			filename = "=?UTF-8?B?"
					+ new BASE64Encoder().encode(filename.getBytes("utf-8"))
					+ "?=";
		} else { // IE及其他浏览器
			filename = URLEncoder.encode(filename, "utf-8");
		}
		return filename;
	}
		
	//上传文件信息
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	//修改前查询
	public String updateForFind(){
		UserService service = new UserService();
		
		try {
			user = service.findById(user.getUserID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "updateForFind_success";
	}
	//根据id查询
	public String findById(){
		UserService service = new UserService();
		
		try {
			user = service.findById(user.getUserID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "findById_success";
	}
	
	//根据id删除
	public String del(){
		//调用Service中条件查询操作
		UserService service = new UserService();
		
		try {
			//先查询用户，判断是否有简历，有，将简历删除
			user = service.findById(user.getUserID());
			String path = user.getPath();
			if (path != null) {
				//有简历，将简历删除
				new File(path).delete();
			}
			service.delById(user.getUserID());
			System.out.println(user.getUserID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "del_success";
	}
	//条件查询
	public String listSelect(){
		
		//调用Service中条件查询操作
		UserService service = new UserService();
		
		try {
			users = service.findBySelect(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "listSelect_success";
	}

	//添加用户
	@InputConfig(resultName = "add_input")
	public String add() throws IOException{
		//1、完成上传简历
		if (upload!=null) {
			String uuidname = FileUploadUtils.getUUIDFileName(uploadFileName);
			File dest = new File("d:/upload",uuidname);
			FileUtils.copyFile(upload, dest);
			//2、调用Service  dao完成添加
			user.setPath("d:/upload/"+uuidname);
			user.setFilename(uploadFileName);
		}
		UserService service = new UserService();
		
		try {
			service.addUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
			this.addActionError("添加失败~");
			return "input";
		}
		
		return "add_success";
	}
	//查询所有
	public String list(){
		UserService service = new UserService();
		try {
			users = service.findAll();
			//手动添加到valueStack
			//ValueStack vs = ActionContext.getContext().getValueStack();
			//vs.set("users", users);
			
			//自动:将users声明成成员变量，提供get/set方法
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return "list_success";
	}
	@InputConfig(resultName = "login_input")//登录校验失败是给login_input跳
	public String login(){
		//调用service中登录的方法
		UserService service = new UserService();
		
		try {
			user = service.login(user.getLogonName(),user.getLogonPwd());
			if(user==null){
				this.addActionError("用户名或密码错误!");
				return Action.LOGIN;
			}
			ServletActionContext.getRequest().getSession().setAttribute("user", user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.addActionError("登录失败");
			return Action.INPUT;
		}
		return "login_success";
	}
}
