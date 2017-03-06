//package com.sortimo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.sortimo.model.User;
//import com.sortimo.repositories.UserRepository;
//
//
///**
// * Created by stephan on 20.03.16.
// */
//@Service
//public class JwtUserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
//        } else {
//            return JwtUserFactory.create(user);
//        }
//    }
//}
