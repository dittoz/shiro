package com.zhouyee.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
/**
 * 自定义Realm需要实现AuthorizingRealm接口
 *
 */
public class MyRealmMd5 extends AuthorizingRealm{
	@Override
	public void setName(String name) {
		super.setName(getClass().getSimpleName());
	}
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//从token中取出身份信息
		String username = (String) token.getPrincipal();
		
		//模拟查询数据库是否存在用户
		if(!"zhouyi".equals(username)){
			return null;//表示用户不存在
		}
		//用户存在从数据库取出密码， 是散列后的密码
		String passwordDb = "d67158eb979de3de68398ad5d27eb2f5";
		//从数据库中取出盐
		String salt = "1993<>?";
		
		return new SimpleAuthenticationInfo(username, passwordDb, ByteSource.Util.bytes(salt), this.getName());
		
		
	}
	//授权，获取授权信息，返回给authorizator
	//自定义realm完成授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取身份信息,身份信息就是认证过程中返回给SimpleAuthenticationInfo的principal信息
		String usercode = (String)principals.getPrimaryPrincipal();
		//模拟查询数据库获取权限信息 ....
		List<String> permissions = new ArrayList<String>();
		permissions.add("user:delete");
		permissions.add("user:create");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissions);
		return info;
	
	}
}
