package com.doddysujatmiko.rumiapi.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findByNameIn(Collection<String> name);
    RoleEntity findByName(String name);
    Boolean existsByName(String name);
}
