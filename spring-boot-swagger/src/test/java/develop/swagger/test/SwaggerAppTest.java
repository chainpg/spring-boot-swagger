package develop.swagger.test;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import develop.service.swagger.app.UserResource;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class SwaggerAppTest {

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new UserResource()).build();
	}

	@Test
	public void testUserController() throws Exception {
		RequestBuilder request = null;

		// 1、get user list
		request = get("/users/");
		mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));
		
		// 2、post a user
		request = post("/users/").param("id", "1").param("name", "Jeremy").param("age", "20");
		mvc.perform(request)
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(content().string(equalTo("success")));

		// 3、get user list
		request = get("/users/");
		mvc.perform(request).andExpect(status().isOk())
				.andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"Jeremy\",\"age\":20}]")));

		// 4、put user of id=1
		request = put("/users/1").param("name", "Jeremy").param("age", "30");
		mvc.perform(request).andExpect(content().string(equalTo("success")));

		// 5、get user of id=1
		request = get("/users/1");
		mvc.perform(request).andExpect(content().string(equalTo("{\"id\":1,\"name\":\"Jeremy\",\"age\":30}")));

		// 6、delete user id=1
		request = delete("/users/1");
		mvc.perform(request).andExpect(content().string(equalTo("success")));

	}

}
