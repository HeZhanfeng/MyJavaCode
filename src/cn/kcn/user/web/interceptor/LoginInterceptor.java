package cn.kcn.user.web.interceptor;

import org.apache.struts2.ServletActionContext;
import cn.kcn.user.damain.User;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LoginInterceptor extends MethodFilterInterceptor{

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		
		User user = (User) ServletActionContext.getRequest().getSession().getAttribute("user");
		
		if (user==null) {
			//没有登录
			ActionSupport action = (ActionSupport) invocation.getAction();
			action.addActionError("您没有登录！");
			return Action.LOGIN;
		} else {
			//登录了，放行
			return invocation.invoke();
		}
	}
	
}
