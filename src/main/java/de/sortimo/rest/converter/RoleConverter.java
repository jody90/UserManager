package de.sortimo.rest.converter;

import java.util.HashSet;
import java.util.Set;

import de.sortimo.rest.dto.ExtendedRoleDto;
import de.sortimo.rest.dto.JwtRoleDto;
import de.sortimo.rest.dto.SimpleRoleDto;
import de.sortimo.service.model.Role;

public class RoleConverter {

	private RightConverter rightConverter = new RightConverter();

	public ExtendedRoleDto createFullRoleDto(Role role) {
		
		ExtendedRoleDto extendedRoleDto = new ExtendedRoleDto();
		extendedRoleDto.setId(role.getId());
		extendedRoleDto.setCreated(role.getCreateDate());
		extendedRoleDto.setModified(role.getModifyDate());
		extendedRoleDto.setName(role.getName());
		extendedRoleDto.setDescription(role.getDescription());
		extendedRoleDto.setRights(rightConverter.createDtoList(role.getRights()));
		
		return extendedRoleDto;
	}
	
	public SimpleRoleDto createPreviewDto(Role role) {
		
		SimpleRoleDto simpleRoleDto = new SimpleRoleDto();
		simpleRoleDto.setId(role.getId());
		simpleRoleDto.setCreated(role.getCreateDate());
		simpleRoleDto.setModified(role.getModifyDate());
		simpleRoleDto.setName(role.getName());
		simpleRoleDto.setDescription(role.getDescription());
		
		return simpleRoleDto;
	}
	
	public JwtRoleDto createJwtRoleDto(Role role) {
		
		JwtRoleDto jwtRoleDto = new JwtRoleDto();
		jwtRoleDto.setId(role.getId());
		jwtRoleDto.setCreated(role.getCreateDate());
		jwtRoleDto.setModified(role.getModifyDate());
		jwtRoleDto.setName(role.getName());
		jwtRoleDto.setDescription(role.getDescription());
		jwtRoleDto.setRights(rightConverter.createJwtRightDtoList(role.getRights()));
		
		return jwtRoleDto;
	}

	public Set<SimpleRoleDto> createDtoList(Iterable<Role> tRoles) {
		
		Set<SimpleRoleDto> rolesList = new HashSet<>();
		
		for (Role role : tRoles) {
			rolesList.add(this.createPreviewDto(role));
		}
		
		return rolesList;
	}

	public Set<JwtRoleDto> createJwtRoleDtoList(Set<Role> tRoles) {
		
		Set<JwtRoleDto> rolesList = new HashSet<>();
		
		for (Role role : tRoles) {
			rolesList.add(this.createJwtRoleDto(role));
		}
		
		return rolesList;
		
	}

}
