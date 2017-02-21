package com.sortimo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sortimo.model.User;

import org.junit.Before;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagerApplication.class)
public class UserManagerApplicationTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

	ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void runTest() throws Exception {
    	User user = new User("demouser", "Demo", "geheim", "User", "demo@user.com");

    	createUser(user);
    	getUser(user.getUsername(), "isOk");
    	User updatedUser = updateUser(user.getUsername());
    	getUser(updatedUser.getUsername(), "isOk");
    	deleteUser(updatedUser.getUsername());
    	getUser(user.getUsername(), "isNotFound");
    }

	public void createUser(User user) throws Exception {

		String jsonInString = mapper.writeValueAsString(user);

		mockMvc.perform(post("/api/user")
			.content(jsonInString)
			.contentType(contentType))
			.andExpect(status().isCreated());
	}
	
	public void getUser(String username, String expectedResponseState) throws Exception {

		if (expectedResponseState.equals("isOk")) {
			mockMvc.perform(get("/api/user/" + username))
			.andExpect(status().isOk());
		}
		else {
			mockMvc.perform(get("/api/user/" + username))
			.andExpect(status().isNotFound());
		}
	}

	public void deleteUser(String username) throws Exception {

		mockMvc.perform(delete("/api/user/" + username))
		    .andExpect(status().isOk());
	}

	public User updateUser(String username) throws Exception {

		User user = new User(username, "DemoTest", "geheimer", "UserTest", "demouser@user.com");

		String jsonInString = mapper.writeValueAsString(user);

		mockMvc.perform(put("/api/user/" + username)
			.content(jsonInString)
			.contentType(contentType))
			.andExpect(status().isOk());

		return user;
	}

}
