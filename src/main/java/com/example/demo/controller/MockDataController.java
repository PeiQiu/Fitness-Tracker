package com.example.demo.controller;

import java.util.*;

import javax.annotation.PostConstruct;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repositories.User.UserRepositoryMongo;
import com.example.demo.services.UserService;
import com.example.demo.tools.MD5Util;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MockDataController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepositoryMongo userRepository;


	@PostConstruct
	private void createMockData() {
		createUsers();

	}
	


	private void createUsers() {
		if (userRepository.count() > 0) {
			System.out.println("users already exist");
			return;
		}		
		
		List<String> firsts = Arrays.asList(new String[] { "Kenny", "Jill", "Karen", "Jim", "William", "Jose", "Amanda", "Deantye", "Sheila" });
		List<String> lasts = Arrays.asList(new String[] { "Hunt", "Geringer", "Alvarez", "Hodgkins", "Murphy", "Gorski", "Doe", "White", "Jones" });
		//List<Role> roles = Arrays.asList(new Role[] { new Role("ROLE_EMPLOYER"), new Role("ROLE_ADMIN") });
		Set<String> userNames = new HashSet<String>();

		for (int i = 0; i < 25; i++) {
			//Collections.shuffle(roles);
			Collections.shuffle(firsts);
			Collections.shuffle(lasts);

			//List<Role> userRoles = roles.stream().limit((int) (Math.random() * 2 + 1)).collect(Collectors.toList());
			List<Role> userRoles = new ArrayList<>();
			userRoles.add(new Role("ROLE_USER"));

			String firstName = firsts.get(0);
			String lastName = lasts.get(0);
			String userName = (firstName.charAt(0) + lastName).toLowerCase();
			String email = lastName + "." + firstName + "@test.org";


			User user = new User.Builder().firstname(firstName).lastname(lastName).authorities(userRoles)
										.status(true).validateCode(MD5Util.encode2hex(email)).registerDate(new Date())
										.email(email).password("123").username(userName).build();

			try {
				if (!userNames.contains(userName)) {
					user = userService.Save(user);
					userNames.add(userName);
				}
			} catch (Exception e) {
				userNames.add(userName);
			}
		}
	}



	private Date randomDate() {
		Calendar c = Calendar.getInstance();
		Integer year = (int) (Math.random() * 30 + 1985);
		Integer month = (int) (Math.random() * 12);
		Integer day = (int) (Math.random() * 29);
		c.set(year, month, day);
		return c.getTime();
	}

}