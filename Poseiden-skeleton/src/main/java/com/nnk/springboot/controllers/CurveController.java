package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class CurveController {
    private LocalDateTime localDateTime;
    private static final Logger logger = LogManager.getLogger(CurveController.class);


    @Autowired
    CurvePointRepository curvePointRepository;

    /**
     * get list of curvepoint
     * @param model
     * @return
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("Display of curvepoint's list successful");
        return "curvePoint/list";
    }

    /**
     * show add form
     * @param bid
     * @return
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    /**
     * store data when valid
     * @param curvePoint
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            localDateTime = LocalDateTime.now();
            curvePoint.setAsOfDate(localDateTime);
            curvePoint.setCreationDate(localDateTime);
            curvePointRepository.save(curvePoint);
            model.addAttribute("curvePoints", curvePointRepository.findAll());
            logger.info("curvepoint adding validation successful");
            logger.info(curvePoint.toString());
            return "redirect:/curvePoint/list";
        }
        logger.info("Error on curvepoint adding validation, go back to adding form");
        logger.info(curvePoint.toString());
        return "redirect:/curvePoint/add";
    }

    /**
     * show update form with data by id
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curve point Id:" + id));
        model.addAttribute("curvePoint", curvePoint);
        logger.info("Bidlist's updating form display successful");
        logger.info(curvePoint.toString());
        return "curvePoint/update";
    }

    /**
     * update data by id
     * @param id
     * @param curvePoint
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            logger.info("Curvepoint updating failed, go back to updating form display, has errors");
            return "redirect:/curvePoint/update";
        }
        curvePoint.setId(id);
        curvePointRepository.save(curvePoint);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("Curvepoint saved successfully, model updated");
        logger.info(curvePoint.toString());
        return "redirect:/curvePoint/list";
    }

    /**
     * delete data by id
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curve point Id:" + id));
        curvePointRepository.delete(curvePoint);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("Curvepoint deleted successfully, model updated");
        logger.info(curvePoint.toString());
        return "redirect:/curvePoint/list";
    }
}
