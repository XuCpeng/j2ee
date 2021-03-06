package cn.medemede.spm.service;

import cn.medemede.spm.enums.ResultEnum;
import cn.medemede.spm.model.*;
import cn.medemede.spm.repository.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户处理
 *
 * @author Saber
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordService passwordService;


    @Resource
    private JUserRole2Repository jUserRole2Repository;

    @Resource
    private JRolePerm2Repository jRolePerm2Repository;

    @Resource
    private AcProveRepository acProveRepository;

    @Resource
    private StuRepository stuRepository;

    public Set<String> getRolesStringSet(String stuId) {
        Set<String> roleStringSet = new HashSet<>();
        for (JUserRole2 jUserRole2 : jUserRole2Repository.findByStuId(stuId)) {
            roleStringSet.add(jUserRole2.getRoleName());
        }
        return roleStringSet;
    }

    public Set<String> getPermsStringSet(String stuId) {
        Set<String> permsStringSet = new HashSet<>();
        for (String roleName : getRolesStringSet(stuId)) {
            for (JRolePerm2 jRolePerm2 : jRolePerm2Repository.findByRoleName(roleName)) {
                permsStringSet.add(jRolePerm2.getPermName());
            }
        }
        return permsStringSet;
    }

    public Result addUser(User user, JUserRole2 jUserRole2, AcProve acProve) {

        Result result = new Result();
        if (userRepository.findOne(user.getStuId()) != null) {
            result.setResultEnum(ResultEnum.USER_HEAD);
        } else {
            user.setPwd(passwordService.UpdatePass(user.getStuId(), user.getPwd()));
            userRepository.save(user);
            jUserRole2Repository.save(jUserRole2);
            acProveRepository.save(acProve);
            Stu stu = new Stu();
            stu.setByAcProve(acProve);
            stuRepository.save(stu);
            result.setResultEnum(ResultEnum.SAVEUSER_SUCCESS);
        }

        return result;
    }
}
