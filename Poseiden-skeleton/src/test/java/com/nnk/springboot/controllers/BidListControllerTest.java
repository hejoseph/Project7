package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql")
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class BidListControllerTest {
    BidList bidList = new BidList(1, "testAccount", "testType", 1.0);

    @Autowired
    BidListRepository bidListDAO;

    @Autowired
    MockMvc mockmvc;

    @Test
    public void getBidListTest() throws Exception {
        this.mockmvc.perform(get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getBidFormTest() throws Exception {
        this.mockmvc.perform(get("/bidList/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateTest() throws Exception {
        this.mockmvc.perform(post("/bidList/update/" + bidList.getId())
                .param("account", bidList.getAccount()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"))
                .andDo(MockMvcResultHandlers.print());
        BidList bidFound = bidListDAO.findOneById(bidList.getId());
        assertEquals(bidList.getAccount(), bidFound.getAccount());
    }

    @Test
    public void deleteTest() throws Exception {
        this.mockmvc.perform(get("/bidList/delete/" + bidList.getId()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"))
                .andDo(MockMvcResultHandlers.print());
        BidList bidFound = bidListDAO.findOneById(bidList.getId());
        assertNull(bidFound);
    }

}
