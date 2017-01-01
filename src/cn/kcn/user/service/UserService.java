package cn.kcn.user.service;

import java.sql.SQLException;
import java.util.List;

import javassist.expr.NewArray;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.kcn.user.damain.User;
import cn.kcn.user.dao.UserDao;
import cn.kcn.user.utils.DBUtils;

public class UserService {
	//登录操作
	public User login(String logonName, String logonPwd) throws SQLException {
		return new UserDao().findUserByNameAndPwd(logonName,logonPwd);
	}
	//查询所有
	public List<User> findAll() throws SQLException {
		return new UserDao().findAll();
	}
	//添加用户
	public void addUser(User user) throws SQLException {
		new UserDao().addUser(user);
	}
	//条件查询
	public List<User> findBySelect(User user) throws SQLException {
		return new UserDao().findBySelect(user);
	}
	//根据ID删除
	public void delById(int userID) throws SQLException {
		new UserDao().delById(userID);
	}
	//根据ID查询
	public User findById(int userID) throws SQLException {
		return new UserDao().findById(userID);
	}
	

}
