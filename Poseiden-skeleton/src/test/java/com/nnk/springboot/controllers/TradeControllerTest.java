package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeControllerTest {
    Trade trade = new Trade(1, "testAccount", "testType");

    @Autowired
    TradeRepository tradeDAO;

    @Autowired
    MockMvc mockmvc;

    @Test
    public void getTradeList() throws Exception {
        this.mockmvc.perform(get("/trade/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getTradeAdd() throws Exception {
        this.mockmvc.perform(get("/trade/add"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateTrade() throws Exception {
        this.mockmvc.perform(post("/trade/update/"+trade.getTradeId())
                .param("account", trade.getAccount()).param("type", trade.getType()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"))
                .andDo(MockMvcResultHandlers.print());
        Trade tradeFound = tradeDAO.findOneByTradeId(trade.getTradeId());
        assertEquals(trade.getAccount(), tradeFound.getAccount());
    }

    @Test
    public void updateTradeError() throws Exception {
        this.mockmvc.perform(post("/trade/update/"+trade.getTradeId())
                .param("account", "").param("type", trade.getType()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/update"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteTrade() throws Exception {
        this.mockmvc.perform(get("/trade/delete/"+trade.getTradeId()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"))
                .andDo(MockMvcResultHandlers.print());
        Trade tradeFound = tradeDAO.findOneByTradeId(trade.getTradeId());
        assertNull(tradeFound);
    }

    @Test
    public void validTradeTest() throws Exception {
        this.mockmvc.perform(post("/trade/validate")
                .param("account", "teest").param("type", "test"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void notValidTradeTest() throws Exception {
        this.mockmvc.perform(post("/trade/validate")
                .param("account", "").param("type", "test"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/add"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void showFormTest() throws Exception {
        this.mockmvc.perform(get("/trade/update/"+trade.getTradeId()))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/update"))
                .andDo(MockMvcResultHandlers.print());
    }

}
