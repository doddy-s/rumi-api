package com.doddysujatmiko.rumiapi.auth;

import com.doddysujatmiko.rumiapi.auth.dtos.Oauth2UserInfoDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @SneakyThrows
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        Oauth2UserInfoDto userInfo = Oauth2UserInfoDto
                .builder()
                .username(oAuth2User.getAttributes().get("username").toString())
                .email(oAuth2User.getAttributes().get("email").toString())
                .picture(oAuth2User.getAttributes().get("picture").toString())
                .build();

        UserEntity user = userRepository.findByUsername(userInfo.getUsername());

        if(user != null) updateExistingUser(user, userInfo);
        else registerNewUser(oAuth2UserRequest, userInfo);

        return oAuth2User;
    }

    private UserEntity registerNewUser(OAuth2UserRequest oAuth2UserRequest, Oauth2UserInfoDto userInfo) {
        UserEntity user = new UserEntity();

        user.setUsername(userInfo.getUsername());
        user.setEmail(userInfo.getEmail());
        user.setPicture(userInfo.getPicture());
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private UserEntity updateExistingUser(UserEntity existingUser, Oauth2UserInfoDto userInfo) {
        existingUser.setUsername(userInfo.getUsername());
        existingUser.setPicture(userInfo.getPicture());
        return userRepository.save(existingUser);
    }
}