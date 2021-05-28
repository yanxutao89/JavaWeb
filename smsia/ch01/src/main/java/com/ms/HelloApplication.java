package com.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping(value="hello")
public class HelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

	@GetMapping(value="/{firstName}/{lastName}")
	public String helloGET( 
			@PathVariable("firstName") String firstName,
			@PathVariable("lastName") String lastName) {
		return String.format("{\"message\":\"Hello %s %s\"}", firstName, lastName);
	}
	
	@PostMapping
	public String helloPOST(@RequestBody HelloRequest request) {
		return String.format("{\"message\":\"Hello %s %s\"}", request.getFirstName(), request.getLastName());
	}
}

class HelloRequest {
	
	private String firstName;
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}

