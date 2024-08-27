package com.doddysujatmiko.rumiapi.auth;

import com.doddysujatmiko.rumiapi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity extends BaseEntity implements GrantedAuthority {
    @Column(unique = true)
    private String name;

    @ManyToMany(targetEntity = UserEntity.class, mappedBy = "roles", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
