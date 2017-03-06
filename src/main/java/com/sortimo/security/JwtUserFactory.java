package com.sortimo.security;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.sortimo.model.Right;
import com.sortimo.model.User;


public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                (long) 1, 
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRights()),
                true,
                new Date()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Right> set) {
        return set.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }
}
