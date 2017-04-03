package de.sortimo.rest.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.sortimo.rest.dto.ExtendedUserDto;
import de.sortimo.rest.dto.JwtUserDto;
import de.sortimo.rest.dto.SimpleUserDto;
import de.sortimo.service.UserSpringSecurityService;
import de.sortimo.service.model.User;

@Service
public class UserConverter {
	
	private RightConverter rightConverter = new RightConverter();
	
	private RoleConverter roleConverter = new RoleConverter();
	
	private UserSpringSecurityService authoritiesService = new UserSpringSecurityService();
	
	public JwtUserDto getJwtUser(User user) {
		
		JwtUserDto jwtUser = new JwtUserDto();
		jwtUser.setUsername(user.getUsername());
		jwtUser.setFirstname(user.getFirstname());
		jwtUser.setLastname(user.getLastname());
		jwtUser.setEmail(user.getEmail());
		jwtUser.setPassword(user.getPassword());
		jwtUser.setRights(rightConverter.createJwtRightDtoList(user.getRights()));
		jwtUser.setRoles(roleConverter.createJwtRoleDtoList(user.getRoles()));
		jwtUser.setAuthorities(authoritiesService.getAuthorities(user));
		
		return jwtUser;
		
	}

	public ExtendedUserDto createFullDto(User user) {
		
		ExtendedUserDto extendedUserDto = new ExtendedUserDto();
		extendedUserDto.setId(user.getId());
		extendedUserDto.setCreated(user.getCreateDate());
		extendedUserDto.setModified(user.getModifyDate());
		extendedUserDto.setUsername(user.getUsername());
		extendedUserDto.setFirstname(user.getFirstname());
		extendedUserDto.setLastname(user.getLastname());
		extendedUserDto.setEmail(user.getEmail());
		extendedUserDto.setRights(rightConverter.createDtoList(user.getRights()));
		extendedUserDto.setRoles(roleConverter.createDtoList(user.getRoles()));
		
		return extendedUserDto;
	}
	
	public SimpleUserDto createPreviewDto(User user) {
		
		SimpleUserDto simpleUserDto = new SimpleUserDto();
		simpleUserDto.setId(user.getId());
		simpleUserDto.setCreated(user.getCreateDate());
		simpleUserDto.setModified(user.getModifyDate());
		simpleUserDto.setUsername(user.getUsername());
		simpleUserDto.setFirstname(user.getFirstname());
		simpleUserDto.setLastname(user.getLastname());
		simpleUserDto.setEmail(user.getEmail());
		
		return simpleUserDto;
	}

	public List<SimpleUserDto> createDtoPreviewList(Iterable<User> tUser) {
		
		List<SimpleUserDto> usersList = new ArrayList<>();
		
		for (User user : tUser) {
			usersList.add(this.createPreviewDto(user));
		}
		
		return usersList;
	}
	
	
}
