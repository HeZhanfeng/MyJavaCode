package cn.kcn.user.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javassist.expr.NewArray;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.kcn.user.damain.User;
import cn.kcn.user.utils.DBUtils;	

public class UserDao {
	// 登录
	public User findUserByNameAndPwd(String logonName, String logonPwd)
			throws SQLException {
		String sql = "select * from s_user where logonName=? and logonPwd=?";

		QueryRunner runner = new QueryRunner(DBUtils.getDataSource());
		// BeanHandler<User>(User.class)将结果集中的第一行数据封装到一个对应的JavaBean实例中
		return runner.query(sql, new BeanHandler<User>(User.class), logonName,
				logonPwd);
		
	}

	// 查询所有
	public List<User> findAll() throws SQLException {
		String sql = "select * from s_user";
		QueryRunner runner = new QueryRunner(DBUtils.getDataSource());
		// BeanListHandler<User>(User.class)将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里
		return runner.query(sql, new BeanListHandler<User>(User.class));
	}

	// 添加操作
	public void addUser(User user) throws SQLException {
		String sql = "insert into s_user values(null,?,?,?,?,?,?,?,?,?,?,?)";
		QueryRunner runner = new QueryRunner(DBUtils.getDataSource());

		runner.update(sql, user.getUserName(), user.getLogonName(),
				user.getLogonPwd(), user.getSex(), user.getBirthday(),
				user.getEducation(), user.getTelephone(), user.getInterest(),
				user.getPath(), user.getFilename(), user.getRemark());
	}
	//条件查询
	public List<User> findBySelect(User user) throws SQLException {
		
		String sql = "select * from s_user where 1=1 ";//这里写1=1是为了下面不必在每个and前面加where
		List<Object> params = new ArrayList<Object>();//用来装查询参数
		
		String username = user.getUserName();
		if (username != null&& username.trim().length() > 0) {
			sql+=" and userName like ?";
			params.add("%"+username+"%");
		}
		String sex = user.getSex();
		if (sex != null&&sex.trim().length() > 0) {
			sql+=" and sex=?";
			params.add(sex);
		}
		String education = user.getEducation();
		if (education !=null&&education.trim().length() > 0) {
			sql += " and education=?";
			params.add(education);
		}
		String isupload = user.getIsUpload();
		if ("1".equals(isupload)) {
			sql+=" and filename is not null";
		} else if ("2".equals(isupload)) {
			sql+=" and filename is null";
		}
		
		QueryRunner runner = new QueryRunner(DBUtils.getDataSource());
		
		return runner.query(sql, new BeanListHandler<User>(User.class),params.toArray());
	}
	//根据id删除
	public void delById(int userID) throws SQLException {
		String sql = "delete from s_user where userID=?";
		QueryRunner runner = new QueryRunner(DBUtils.getDataSource());
		runner.update(sql,userID);
	}
	//根据ID查询
	public User findById(int userID) throws SQLException {
		String sql = "select * from s_user where userID=?";
		QueryRunner runner = new QueryRunner(DBUtils.getDataSource());
		
		return runner.query(sql, new BeanHandler<User>(User.class),userID);
	}

}
