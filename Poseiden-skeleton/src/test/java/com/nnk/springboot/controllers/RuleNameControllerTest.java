package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Sql(scripts = "classpath:data-test.sql")
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class RuleNameControllerTest {
    RuleName ruleName = new RuleName(1, "testName", "testDesc", "testJson", "testTemplate", "testStr", "testPart");

    @Autowired
    RuleNameRepository ruleNameDAO;

    @Autowired
    MockMvc mockmvc;

    @Test
    public void getRuleNameList() throws Exception {
        this.mockmvc.perform(get("/ruleName/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getRuleNameAdd() throws Exception {
        this.mockmvc.perform(get("/ruleName/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateRuleName() throws Exception {
        this.mockmvc.perform(post("/ruleName/update/"+ruleName.getId())
                .param("name", ruleName.getName()))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"))
                .andDo(MockMvcResultHandlers.print());
        RuleName ruleNameFound = ruleNameDAO.findOneById(ruleName.getId());
        assertEquals(ruleName.getName(), ruleNameFound.getName());
    }

    @Test
    public void deleteRuleName() throws Exception {
        this.mockmvc.perform(get("/ruleName/delete/"+ruleName.getId()))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"))
                .andDo(MockMvcResultHandlers.print());
    }

}

