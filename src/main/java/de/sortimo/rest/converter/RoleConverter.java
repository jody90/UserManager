package de.sortimo.rest.converter;

import java.util.ArrayList;
import java.util.List;

import de.sortimo.rest.dto.SimpleRoleDto;
import de.sortimo.service.model.Role;

public class RoleConverter {
	
	public SimpleRoleDto createDto(Role role) {
		
		SimpleRoleDto simpleRoleDto = new SimpleRoleDto();
		simpleRoleDto.setId(role.getId());
		simpleRoleDto.setCreated(role.getCreateDate());
		simpleRoleDto.setModified(role.getModifyDate());
		simpleRoleDto.setName(role.getName());
		simpleRoleDto.setDescription(role.getDescription());
		simpleRoleDto.setRights(role.getRights());
		
		return simpleRoleDto;
	}

	public List<SimpleRoleDto> createDtoList(Iterable<Role> tRoles) {
		
		List<SimpleRoleDto> rolesList = new ArrayList<>();
		
		for (Role role : tRoles) {
			rolesList.add(this.createDto(role));
		}
		
		return rolesList;
	}

}
