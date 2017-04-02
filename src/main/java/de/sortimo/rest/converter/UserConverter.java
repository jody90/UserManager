package de.sortimo.rest.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sortimo.rest.dto.SimpleUserDto;
import de.sortimo.service.UserSpringSecurityService;
import de.sortimo.service.model.User;
import de.sortimo.service.security.JwtUser;

@Service
public class UserConverter {
	
	@Autowired
	private UserSpringSecurityService userObjectService;
	
	public JwtUser getJwtUser(User user) {
		return new JwtUser(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRights(), user.getRoles(), userObjectService.getAuthorities(user));
	}
	
	public SimpleUserDto createPreviewDto(User user) {
		
		SimpleUserDto simpleUserDto = new SimpleUserDto();
		simpleUserDto.setId(user.getId());
		simpleUserDto.setCreated(user.getCreateDate().toString());
		simpleUserDto.setModified(user.getModifyDate().toString());
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
