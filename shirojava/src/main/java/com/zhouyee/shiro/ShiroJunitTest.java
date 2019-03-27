package com.zhouyee.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class ShiroJunitTest {
	/**
认证流程
1. 通过ini文件创建securityManage
2. 调用subject.login方法提交认证，提交token信息
3. securityManage最终由ModularRealmAuthenticator认证
4. ModularRealmAuthenticator调用inirealm去ini配置文件中查询用户信息
5. inirealm根据输入的token从shiro-first.ini中查询用户信息
如果查询到用户信息，就给ModularRealmAuthenticator返回用户信息（账号和密码）
如果查询不到用户信息，就返回空
6. ModularRealmAuthenticator接受iniRealm返回的Authentication认证信息
如果返回的是空，抛出异常org.apache.shiro.authc.UnknownAccountException
如果返回的不是空，对inirealm返回的用户密码和token的密码比对，不一致抛出异常org.apache.shiro.authc.IncorrectCredentialsException
	 * ֤
	 */
	@Test
	public void test(){
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("zhang", "123");
		try{
			subject.login(token);	
		}catch(AuthenticationException e){
			e.printStackTrace();
		}
		System.out.println("认证通过:"+subject.isAuthenticated());
		subject.logout();
	}
	//自定义realm认证测试
	@Test
	public void testCustomRealm(){
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro-first.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("zhangsan", "123");
		try{
			subject.login(token);	
		}catch(AuthenticationException e){
			e.printStackTrace();
		}
		System.out.println("认证通过:"+subject.isAuthenticated());
		subject.logout();
	}
	//定义md5加密
	@Test
	public void testCustomRealmMd5(){
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro-md5.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("zhouyi", "111111");
		try{
			subject.login(token);	
		}catch(AuthenticationException e){
			e.printStackTrace();
		}
		System.out.println("认证通过:"+subject.isAuthenticated());
		subject.logout();
	}
	//自定义realm授权测试
	@Test
	public void test1() {
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro-realm.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject currentUser=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("zhouyi", "111111");
		try{
			currentUser.login(token);	
		}catch(AuthenticationException e){
			e.printStackTrace();
		}
		//认证通过后，授权检查
		System.out.println("认证通过:"+currentUser.isAuthenticated());
		//三种授权的方式
		/**
		 * 1. subject.isPermission方法检查
		 * 2. 注解式 
		 * @RequiresRoles("role1")
		 * public void test() {}
		 * 3. JSP/GSP标签中
		 * <shiro:hasRole name="admin">
<!— 有权限—>
</shiro:hasRole>
		 */
		
		/**
		 * 自定义realm授权的流程
		 * 1. 调用subject的 isPermission 授权检查
		 * 2. 实际上是SecurityManager执行授权，通过ModularRealmAuthorizer授权
		 * 3. ModularRealmAuthorizer 通过自定义的realm从数据库中获取授权信息
		 * 4. ModularRealmAuthorizer进行权限比对
		 */
		System.out.println(currentUser.hasRole("role1"));
		System.out.println(currentUser.isPermitted("user:create"));
		currentUser.logout();
	}
}
