package com.sortimo.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sortimo.model.Role;
import com.sortimo.repositories.RightRepository;
import com.sortimo.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RightRepository rightRepo;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		com.sortimo.model.User user = userRepo.findByUsername(username);
		
		Collection<GrantedAuthority> authorities = buildUserAuthority(user.getRoles(), user);

		return buildUserForAuthentication(user, authorities);
	}

	private User buildUserForAuthentication(com.sortimo.model.User user, Collection<GrantedAuthority> authorities) {

		return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);

	}

	private Collection<GrantedAuthority> buildUserAuthority(Set<Role> roles, com.sortimo.model.User user) {
		
		System.out.println(user);
		
		Collection<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		
		// Alles Rechte zu einer Rolle holen
//		for (Role userRole : roles) {
//			rightRepo
//		}
		
		
		// Build user's authorities
		for (Role userRole : roles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
		}		

		return setAuths;
	}

}
