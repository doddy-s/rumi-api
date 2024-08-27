package com.doddysujatmiko.rumiapi.auth.utils;

import com.doddysujatmiko.rumiapi.auth.UserEntity;
import com.doddysujatmiko.rumiapi.auth.UserRepository;
import com.doddysujatmiko.rumiapi.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsManagerImpl implements UserDetailsManager {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(UserDetails user) {
        ((UserEntity) user).setPassword(passwordEncoder.encode(user.getPassword()));

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already used");
        }

        userRepository.save((UserEntity) user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User " + username + " not found");

        return user;
    }
}
