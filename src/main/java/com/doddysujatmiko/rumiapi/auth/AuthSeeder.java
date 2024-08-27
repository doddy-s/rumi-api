package com.doddysujatmiko.rumiapi.auth;

import com.doddysujatmiko.rumiapi.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Component
@Service
public class AuthSeeder implements ApplicationRunner {
    @Autowired
    LogService logService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    RoleRepository roleRepository;

    private final String[] roles = {
            "ROLE_USER",
            "ROLE_ADMIN",
            "ROLE_SUPERUSER"
    };

    private final String[] users = {
            "user user@rumi user ROLE_USER",
            "admin admin@rumi admin ROLE_ADMIN",
            "superuser superuser@rumi superuser ROLE_SUPERUSER"
    };

    @Override
    public void run(ApplicationArguments args) {
        insertRoles();
        insertUsers();
    }

    private void insertRoles() {
        logService.logInfo("Seeding roles table");
        for(String role : roles) {
            if(roleRepository.existsByName(role)) {
                continue;
            }

            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(role);

            roleRepository.save(roleEntity);
        }
    }

    private void insertUsers() {
        logService.logInfo("Seeding users table");
        for(String user : users) {
            String[] strings = user.split(" ");

            if(userRepository.existsByUsername(strings[0])) {
                continue;
            }

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(strings[0]);
            userEntity.setEmail(strings[1]);
            userEntity.setPassword(strings[2]);

            userEntity.setRoles(roleRepository.findByNameIn(Collections.singleton(strings[3])));

            userDetailsManager.createUser(userEntity);
        }
    }
}
