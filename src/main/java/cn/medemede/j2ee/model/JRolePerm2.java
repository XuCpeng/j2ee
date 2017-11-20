package cn.medemede.j2ee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@lombok.Data
@Table(name = "j_role_perm2", schema = "j2ee")
public class JRolePerm2 {

    @Id
    @GeneratedValue
    private Integer id;

    private String roleName;

    private String permName;

}
