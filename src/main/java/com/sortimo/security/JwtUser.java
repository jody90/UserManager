//package com.sortimo.security;
//
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.sortimo.model.User;
//
//public class JwtUser implements UserDetails, UserDetailsService {
//
//	private static final long serialVersionUID = 7803957341175348359L;
//	
//    private final String username;
//    private final String password;
//    private final Collection<? extends GrantedAuthority> authorities;
//    private final boolean enabled;
//    private final User user;
//
//    public JwtUser( String username, String password, Collection<GrantedAuthority> authorities, boolean enabled, User user) {
//        this.username = username;
//        this.password = password;
//        this.authorities = authorities;
//        this.enabled = enabled;
//        this.user = user;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @JsonIgnore
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @JsonIgnore
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @JsonIgnore
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @JsonIgnore
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//    
//    public User getUser() {
//    	return this.user;
//    }
//
//    @JsonIgnore
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
