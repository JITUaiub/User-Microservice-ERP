package com.example.microservice.dbservice.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice.dbservice.model.User;
import com.example.microservice.dbservice.repository.UserRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/rest/db")
public class DBResources {

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	EntityManager em;

	public DBResources() {

	}

	@HystrixCommand(fallbackMethod = "fallBackDB")
	@GetMapping("/{username}")
	public User getUserFromDB(@PathVariable("username") final String username) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> rootEntry = cq.from(User.class);
		CriteriaQuery<User> all = cq.select(rootEntry);
		TypedQuery<User> allQuery = em.createQuery(all);
		List<User> userList = allQuery.getResultList();

		for (User u : userList) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}

	public User fallBackDB(@PathVariable("username") final String username) {

		User defaultUser = new User();
		defaultUser.setId(-1);
		defaultUser.setName("Default User");
		defaultUser.setUsername("Not Exist");
		defaultUser.setPassword("Not Applicable");

		return new User();
	}

	@PostMapping("/add")
	public boolean addUser(@RequestBody final User user) {

		userRepository.save(user);
		return true;
	}

	@DeleteMapping("/delete/{username}")
	public void deleteUser(@PathVariable String username) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> rootEntry = cq.from(User.class);
		CriteriaQuery<User> all = cq.select(rootEntry);
		TypedQuery<User> allQuery = em.createQuery(all);
		List<User> userList = allQuery.getResultList();
		int delID = 0;
		for (User u : userList) {
			if (u.getUsername().equals(username)) {
				delID = u.getId();
				break;
			}
		}

		userRepository.deleteById(delID);
	}

	@PutMapping("/update/{username}")
	public void updateUser(@PathVariable String username,
			@RequestBody final User user) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> rootEntry = cq.from(User.class);
		CriteriaQuery<User> all = cq.select(rootEntry);
		TypedQuery<User> allQuery = em.createQuery(all);
		List<User> userList = allQuery.getResultList();

		for (User u : userList) {
			if (u.getUsername().equals(username)) {
				u.setName(user.getName());
				u.setPassword(user.getPassword());
				u.setUsername(user.getUsername());

				userRepository.save(u);
				return;
			}
		}
	}
}
