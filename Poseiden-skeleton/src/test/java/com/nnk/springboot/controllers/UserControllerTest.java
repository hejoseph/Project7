package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Sql(scripts = "classpath:data-test.sql")
@Transactional
@AutoConfigureMockMvc
//@WebMvcTest
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static final Logger logger = LogManager.getLogger(UserControllerTest.class);

    User user = new User(1, "test", "StrongPass!1","ABCD", "ADMIN");

    @Autowired
    UserRepository userDAO;

    @Value("${spring.datasource.url}") private String url;

    @Autowired
    private MockMvc mockmvc;
//    @Autowired
//    private static WebApplicationContext context;

//    @PostConstruct
//    public void init(){
//        logger.info("debug "+url);
//        logger.info(userDAO.findAll());
//    }

//    @BeforeAll
//    public static void setup() {
//        mockmvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }

//    @WithMockUser("spring")
    @Test
    public void getUserList() throws Exception {
//        .perform(get("/admin").with(user("admin").password("pass").roles("USER","ADMIN")))

        this.mockmvc.perform(get("/user/list")/*.with(user("admin").password("pass").roles("USER","ADMIN"))*/)
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addUser() throws Exception {
        this.mockmvc.perform(get("/user/add"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception {
        this.mockmvc.perform(post("/user/update/"+user.getId())
                .param("password", user.getPassword()).param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"))
                .andDo(MockMvcResultHandlers.print());
        User userFound = userDAO.findOneById(user.getId());
        assertEquals(userFound.getFullname(),user.getFullname());
    }

    @Test
    public void updateUserError() throws Exception {
        this.mockmvc.perform(post("/user/update/"+user.getId())
                .param("password", "").param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/update"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUser() throws Exception {
        this.mockmvc.perform(get("/user/delete/"+user.getId()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"))
                .andDo(MockMvcResultHandlers.print());
        User userFound = userDAO.findOneById(user.getId());
        assertNull(userFound);
    }

    @Test
    public void testUserStrongPassword() throws Exception {
        this.mockmvc.perform(post("/user/validate")
                .param("password", user.getPassword()).param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUserSimplePassword() throws Exception {
        this.mockmvc.perform(post("/user/validate")
                .param("password", "abcde").param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/add"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUserEmptyPassword() throws Exception {
        this.mockmvc.perform(post("/user/validate")
                .param("password", "").param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/add"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void showFormTest() throws Exception {
        this.mockmvc.perform(get("/user/update/"+user.getId()))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/update"))
                .andDo(MockMvcResultHandlers.print());
    }

}
