package com.sortimo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sortimo.converter.JwtUserConverter;
import com.sortimo.model.Right;
import com.sortimo.model.Role;
import com.sortimo.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		com.sortimo.model.User userOrginal = userRepo.findByUsername(username);
		
		JwtUser user = new JwtUserConverter().getJwtUser(userOrginal);

		Collection<GrantedAuthority> authorities = buildUserAuthority(user);

		return buildUserForAuthentication(user, authorities);
	}

	public UserDetails buildUserFromToken(JwtUser user) throws UsernameNotFoundException {
		
		Collection<GrantedAuthority> authorities = buildUserAuthority(user);

		return buildUserForAuthentication(user, authorities);
	}
	
	private User buildUserForAuthentication(JwtUser user, Collection<GrantedAuthority> authorities) {
		
		return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);

	}

	private Collection<GrantedAuthority> buildUserAuthority(JwtUser user) {
		
		Collection<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		
		List<Right> allRights = new ArrayList<>();
		
		if (user.getRights() != null) {
			for (Right right : user.getRights()) {
				allRights.add(right);
			}
		}
		
		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				if (role.getRights() != null) {
					for (Right right : role.getRights()) {
						if (!allRights.contains(right)) {
							allRights.add(right);
						}
					}
				}
			}
		}
		
		// Build user's authorities
		for (Right userRight : allRights) {
			setAuths.add(new SimpleGrantedAuthority(userRight.getName()));
		}		

		return setAuths;
	}

}
