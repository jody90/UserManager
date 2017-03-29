package de.sortimo.services.object;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.sortimo.model.Right;
import de.sortimo.model.Role;
import de.sortimo.model.User;
import de.sortimo.services.Timelog;

@Service
public class UserObjectService {

	/*
	 * Generiert eine Liste alles Authorities
	 */
	@Timelog
	public List<Right> getAuthorities(User user) {
		
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
		
		return allRights;
		
	}
	
}
