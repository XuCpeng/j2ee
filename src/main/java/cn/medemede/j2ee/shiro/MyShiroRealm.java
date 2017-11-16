package cn.medemede.j2ee.shiro;

import cn.medemede.j2ee.enums.ResultEnum;
import cn.medemede.j2ee.exception.MyException;
import cn.medemede.j2ee.model.Role;
import cn.medemede.j2ee.model.User;
import cn.medemede.j2ee.repository.UserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service()
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user= userRepository.findOne((String) principals.getPrimaryPrincipal());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setRoles(user.getRolesStringSet());
            for (Role role : user.getRoleSet()) {
                info.addStringPermissions(role.getPermsStringSet());
            }
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //强转
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //获取username,即stuId
        String username = upToken.getUsername();

        User user=userRepository.findOne(username);

        if(user==null){
            throw new MyException(ResultEnum.Unknown_Account);
        }
        return new SimpleAuthenticationInfo(username, user.getPwd(), ByteSource.Util.bytes(username), getName());
    }
}