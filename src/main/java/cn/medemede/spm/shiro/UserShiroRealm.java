package cn.medemede.spm.shiro;

import cn.medemede.spm.enums.ResultEnum;
import cn.medemede.spm.exception.UnifiedRuntimeException;
import cn.medemede.spm.model.User;
import cn.medemede.spm.repository.UserRepository;
import cn.medemede.spm.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Shiro的认证Realm
 *
 * @author Saber
 */
@Service
public class UserShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Resource
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String stuId = (String) principals.getPrimaryPrincipal();
        User user = userRepository.findOne(stuId);

        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setRoles(userService.getRolesStringSet(stuId));
            info.addStringPermissions(userService.getPermsStringSet(stuId));
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

        User user = userRepository.findOne(username);

        if (user == null) {
            throw new UnifiedRuntimeException(ResultEnum.Unknown_Account);
        }

        return new SimpleAuthenticationInfo(username, user.getPwd(), ByteSource.Util.bytes(username), getName());
    }


}
