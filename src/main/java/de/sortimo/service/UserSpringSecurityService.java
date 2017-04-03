package de.sortimo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.sortimo.base.aspects.Timelog;
import de.sortimo.rest.converter.RightConverter;
import de.sortimo.rest.dto.JwtRightDto;
import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;
import de.sortimo.service.model.User;

@Service
public class UserSpringSecurityService {
	
	private RightConverter rightConverter = new RightConverter();

	/*
	 * Generiert eine Liste aller Authorities
	 */
	@Timelog
	public List<JwtRightDto> getAuthorities(User user) {
		
		List<JwtRightDto> allRights = new ArrayList<>();
		
		if (user.getRights() != null) {
			for (Right right : user.getRights()) {
				allRights.add(rightConverter.createJwtRightDto(right));
			}
		}
		
		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				if (role.getRights() != null) {
					for (Right right : role.getRights()) {
						if (!allRights.contains(right)) {
							allRights.add(rightConverter.createJwtRightDto(right));
						}
					}
				}
			}
		}
		
		return allRights;
		
	}
	
}
