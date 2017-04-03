package de.sortimo.rest.converter;

import java.util.HashSet;
import java.util.Set;

import de.sortimo.rest.dto.JwtRightDto;
import de.sortimo.rest.dto.SimpleRightDto;
import de.sortimo.service.model.Right;

public class RightConverter {
	
	public SimpleRightDto createDto(Right right) {
		
		SimpleRightDto simpleRightDto = new SimpleRightDto();
		simpleRightDto.setId(right.getId());
		simpleRightDto.setCreated(right.getCreateDate());
		simpleRightDto.setModified(right.getModifyDate());
		simpleRightDto.setName(right.getName());
		simpleRightDto.setDescription(right.getDescription());
		
		return simpleRightDto;
	}
	
	public JwtRightDto createJwtRightDto(Right right) {
		JwtRightDto jwtRightDto = new JwtRightDto();
		jwtRightDto.setId(right.getId());
		jwtRightDto.setCreated(right.getCreateDate());
		jwtRightDto.setModified(right.getModifyDate());
		jwtRightDto.setName(right.getName());
		jwtRightDto.setDescription(right.getDescription());
		
		return jwtRightDto;
	}

	public Set<SimpleRightDto> createDtoList(Iterable<Right> tRights) {
		
		Set<SimpleRightDto> rightsList = new HashSet<>();
		
		for (Right right : tRights) {
			rightsList.add(this.createDto(right));
		}
		
		return rightsList;
	}
	
	public Set<JwtRightDto> createJwtRightDtoList(Iterable<Right> tRights) {
		
		Set<JwtRightDto> rightsList = new HashSet<>();
		
		for (Right right : tRights) {
			rightsList.add(this.createJwtRightDto(right));
		}
		
		return rightsList;
	}

}
