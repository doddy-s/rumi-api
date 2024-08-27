package com.doddysujatmiko.rumiapi.auth;

import com.doddysujatmiko.rumiapi.auth.dtos.LoginReqDto;
import com.doddysujatmiko.rumiapi.auth.dtos.RegisterReqDto;
import com.doddysujatmiko.rumiapi.auth.dtos.UserDto;
import com.doddysujatmiko.rumiapi.log.LogService;
import com.doddysujatmiko.rumiapi.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AuthService {
    @Autowired
    LogService logService;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    ValidationService validationService;

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    UserRepository userRepository;

    public UserDto register(RegisterReqDto req) {
        validationService.validate(req);

        logService.logInfo(req.getUsername() + " is trying to register");

        var user = UserDto.toEntity(req);
        user.setPassword(req.getPassword());

        userDetailsManager.createUser(user);

        return UserDto.fromEntity(userRepository.findByUsername(req.getUsername()));
    }

    public Authentication login(LoginReqDto req) {
        validationService.validate(req);

        logService.logInfo(req.getUsername() + " is trying to log in");

        return daoAuthenticationProvider
                .authenticate(UsernamePasswordAuthenticationToken
                        .unauthenticated(req.getUsername(), req.getPassword()));
    }

    public UserDto readAuthenticatedUser(Principal principal) {
        return UserDto.fromEntity(userRepository.findByUsername(principal.getName()));
    }
}
