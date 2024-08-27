package com.doddysujatmiko.rumiapi.auth.utils;

import com.doddysujatmiko.rumiapi.auth.UserRepository;
import com.doddysujatmiko.rumiapi.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        var user = userRepository.findByUsername(source.getSubject());

        if(user == null) {
            throw new NotFoundException("User not registered");
        }

        return new UsernamePasswordAuthenticationToken(user, source, user.getAuthorities());
    }
}
