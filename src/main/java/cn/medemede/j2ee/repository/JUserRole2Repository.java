package cn.medemede.j2ee.repository;

import cn.medemede.j2ee.model.JUserRole2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Saber
 */
public interface JUserRole2Repository extends JpaRepository<JUserRole2,Integer> {

    List<JUserRole2> findByStuId(String stuId);

    List<JUserRole2> findByRoleName(String roleName);
}
