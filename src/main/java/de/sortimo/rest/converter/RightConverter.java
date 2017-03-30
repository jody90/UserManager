package de.sortimo.rest.converter;

import java.util.ArrayList;
import java.util.List;

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

	public List<SimpleRightDto> createDtoList(Iterable<Right> tRights) {
		
		List<SimpleRightDto> rightsList = new ArrayList<>();
		
		for (Right right : tRights) {
			rightsList.add(this.createDto(right));
		}
		
		return rightsList;
	}

}
