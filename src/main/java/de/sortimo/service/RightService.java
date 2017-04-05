package de.sortimo.service;

import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.sortimo.base.aspects.Timelog;
import de.sortimo.service.model.Right;
import de.sortimo.service.repositories.RightRepository;

@Service
public class RightService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RightService.class);
	
	@Autowired
	private RightRepository rightRepo;

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Iterable<Right>> findAll() {
		Optional<Iterable<Right>> tRights = rightRepo.findAllRights();
		return tRights;
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Right> findByName(String name) {
		return rightRepo.findByName(name);
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Right> update(String rightName, Right right) {
		
		Validate.notNull(right.getName());
		
		Optional<Right> tRight = this.findByName(rightName);
		
		if (tRight.isPresent()) {
			
			Right updateRight = tRight.get();
			
			updateRight.setName(right.getName());
			
			if (StringUtils.isNotEmpty(right.getDescription())) {
				updateRight.setDescription(right.getDescription());
			}
		}
		
		return tRight;
		
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Right right) {
		rightRepo.delete(right);
		LOGGER.info("Recht {} gelöscht.", right.getName());
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Right save(String name, String description) {
		Right finalRight = new Right(name.toLowerCase(), description);
		rightRepo.save(finalRight);
		LOGGER.info("Recht {} gespeichert.", name);
		return finalRight;
	}

}
