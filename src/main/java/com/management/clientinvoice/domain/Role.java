package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;
import com.management.clientinvoice.enumerator.RoleType;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "role",
        indexes = {
                @Index(columnList = "role_type", name = "idx_role_roleType")
        })
@Where(clause = "is_active= true")
@NoArgsConstructor
public class Role extends BaseEntity {

    private static final long serialVersionUID = 8698769252079770712L;

    public Role(String name) {
        this.roleType = RoleType.getEnum(name);
    }


    /**
     * Role type of User
     */
    @Column(name = "role_type", length = DBConstants.ENUM_DEFAULT_MAX_CHAR_LIMIT, unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    /**
     * Extra column1
     */
    @Column(name = "field1")
    private String field1;

    /**
     * Extra column2
     */
    @Column(name = "field2")
    private String field2;

    @OneToMany(mappedBy = "role")
    private List<UserIdentity> userIdentities;


    /**
     * @return the role
     */
    public RoleType getRoleType() {
        return roleType;
    }


    /**
     * @param roleType the role to set
     */
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }


    /**
     * @return the field1
     */
    public String getField1() {
        return field1;
    }

    /**
     * @param field1 the field1 to set
     */
    public void setField1(String field1) {
        this.field1 = field1;
    }

    /**
     * @return the field2
     */
    public String getField2() {
        return field2;
    }

    /**
     * @param field2 the field2 to set
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (getId() != role.getId()) return false;
        if (getRoleType() != role.getRoleType()) return false;
        if (getField1() != null ? !getField1().equals(role.getField1()) : role.getField1() != null) return false;
        return getField2() != null ? getField2().equals(role.getField2()) : role.getField2() == null;
    }

    @Override
    public int hashCode() {
        int result = getRoleType().hashCode();
        result = 31 * result + (getField1() != null ? getField1().hashCode() : 0);
        result = 31 * result + (getField2() != null ? getField2().hashCode() : 0);
        return result;
    }

    public List<UserIdentity> getUserIdentities() {
        return userIdentities;
    }

    public void setUserIdentities(List<UserIdentity> userIdentities) {
        this.userIdentities = userIdentities;
    }

}

