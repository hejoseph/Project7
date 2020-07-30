package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql")
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class RatingControllerTest {
//    Rating rating = new Rating(1, "testMoody", "testSand", "test");

    Rating rating = new Rating(1,"testMoody","testSand","testFitch",1);

    @Autowired
    RatingRepository ratingDAO;

    @Autowired
    MockMvc mockmvc;

    @Test
    public void getRatingList() throws Exception {
        this.mockmvc.perform(get("/rating/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addRating() throws Exception {
        this.mockmvc.perform(get("/rating/add"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void modifyRating() throws Exception {
        this.mockmvc.perform(post("/rating/update/"+rating.getId())
                .param("moodysRating", rating.getMoodysRating()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    public void deleteRating() throws Exception {
        this.mockmvc.perform(get("/rating/delete/"+rating.getId()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"))
                .andDo(MockMvcResultHandlers.print());
        Rating ratingFound = ratingDAO.findOneById(rating.getId());
        assertNull(ratingFound);
    }
}
