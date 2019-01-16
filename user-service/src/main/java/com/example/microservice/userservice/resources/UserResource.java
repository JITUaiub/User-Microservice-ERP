package com.example.microservice.userservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.microservice.userservice.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/rest/user")
public class UserResource {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod="fallBack")
	@GetMapping("/{username}")
	public User getUser(@PathVariable("username") final String username) {

		ResponseEntity<User> responseEntity = restTemplate.exchange(
				"http://db-service/rest/db/" + username, HttpMethod.GET, null,
				new ParameterizedTypeReference<User>() {
				});
		return responseEntity.getBody();
	}
	
	public User fallBack(@PathVariable("username") final String username){
		
		User defaultUser = new User();
		defaultUser.setId(-1);
		defaultUser.setName("Default User");
		defaultUser.setUsername("Not Exist");
		defaultUser.setPassword("Not Applicable");
		
		return new User();
	}
	
	@PostMapping("/add")
	public void addUser(@RequestBody User user) {

		restTemplate.postForLocation("http://db-service/rest/db/add/", user, User.class);
	}
	
	@DeleteMapping("/delete/{username}")
	public void deleteUser(@PathVariable String username) {
		restTemplate.delete("http://db-service/rest/db/delete/" + username);
	}
	
	@PutMapping("/update/{username}")
	public void updateUser(@PathVariable String username, @RequestBody final User user) {
		restTemplate.put("http://db-service/rest/db/update/" + username, user);
	}
}
