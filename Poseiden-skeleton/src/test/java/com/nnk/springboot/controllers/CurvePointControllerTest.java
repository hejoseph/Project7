package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointControllerTest {
    CurvePoint curvePoint = new CurvePoint(1, 123, 1.23, 1.23);

    @Autowired
    CurvePointRepository curvePointDAO;

    @Autowired
    MockMvc mockmvc;

    @Test
    public void getCurvePointList() throws Exception {
        this.mockmvc.perform(get("/curvePoint/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addCurvePoint() throws Exception {
        this.mockmvc.perform(get("/curvePoint/add"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void modifyCurvePoint() throws Exception {
        this.mockmvc.perform(post("/curvePoint/update/" + curvePoint.getId())
                .param("term", curvePoint.getTerm().toString()))
//                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"))
                .andDo(MockMvcResultHandlers.print());
        CurvePoint curveFound = curvePointDAO.findOneById(curvePoint.getId());
        assertEquals(curvePoint.getTerm(), curveFound.getTerm());
    }

    @Test
    public void deleteCurvePoint() throws Exception {
        this.mockmvc.perform(get("/curvePoint/delete/" + curvePoint.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"))
                .andDo(MockMvcResultHandlers.print());
        CurvePoint curveFound = curvePointDAO.findOneById(curvePoint.getId());
        assertNull(curveFound);
    }

}
