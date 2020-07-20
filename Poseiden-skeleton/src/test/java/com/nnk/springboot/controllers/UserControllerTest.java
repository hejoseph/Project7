package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql")
@Transactional
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final Logger logger = LogManager.getLogger(UserControllerTest.class);

    User user = new User(1, "test", "StrongPass!1","ABCD", "ADMIN");

    @Autowired
    UserRepository userDAO;

    @Value("${spring.datasource.url}") private String url;

    @Autowired
    MockMvc mockmvc;

    @PostConstruct
    public void init(){
        logger.info("debug "+url);
        logger.info(userDAO.findAll());
    }

    @Test
    public void getUserList() throws Exception {
        this.mockmvc.perform(get("/user/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addUser() throws Exception {
        this.mockmvc.perform(get("/user/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
    public void deleteUser() throws Exception {
        this.mockmvc.perform(get("/user/delete/"+user.getId()))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"))
                .andDo(MockMvcResultHandlers.print());
        User userFound = userDAO.findOneById(user.getId());
        assertNull(userFound);
    }

//    @Test
    public void testUserSimplePassword() throws Exception {
        this.mockmvc.perform(post("/user/validate")
                .param("password", user.getPassword()).param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/add"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
    public void testUserEmptyPassword() throws Exception {
        this.mockmvc.perform(post("/user/validate")
                .param("password", "").param("fullname", user.getFullname())
                .param("role", user.getRole()).param("username", user.getUsername()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/add"))
                .andDo(MockMvcResultHandlers.print());
    }

}
