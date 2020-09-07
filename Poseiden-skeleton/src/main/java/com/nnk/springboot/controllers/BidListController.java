package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidListController {
    private static final Logger logger = LogManager.getLogger(BidListController.class);

    @Autowired
    BidListRepository bidListRepository;

    /**
     * get list of bid
     * @param model
     * @return
     */
    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListRepository.findAll());
        logger.info("Display of bidlist's list successful");
        return "bidList/list";
    }

    /**
     * show add form
     * @param bid
     * @return
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * store data when valid
     * @param bid
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            LocalDateTime
                    localDateTime =
                    LocalDateTime.now();
            bidList.setBidListDate(localDateTime);
            bidList.setCreationDate(localDateTime);
            bidListRepository.save(bidList);
            model.addAttribute("bidLists", bidListRepository.findAll());
            logger.info("bidlist adding validation successful");
            logger.info(bidList.toString());
            return "redirect:/bidList/list";
        }
        logger.info("Error on bidlist adding validation, go back to adding form");
        logger.info(bidList.toString());
        return "redirect:/bidList/add";
    }

    /**
     * show update form with data
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        model.addAttribute("bidList", bidList);
        logger.info("Bidlist's updating form display successful");
        logger.info(bidList.toString());
        return "bidList/update";
    }

    /**
     * update data by id
     * @param id
     * @param bidList
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            logger.info("Bidlist updating failed, go back to updating form display, has errors");
            return "redirect:/bidList/update";
        }
        bidList.setBidListId(id);
        bidListRepository.save(bidList);
        model.addAttribute("bidLists", bidListRepository.findAll());
        logger.info("Bidlist saved successfuly, model updated");
        logger.info(bidList.toString());
        return "redirect:/bidList/list";
    }

    /**
     * delete data by id
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bidlist Id:" + id));
        bidListRepository.delete(bidList);
        model.addAttribute("bidLists", bidListRepository.findAll());
        logger.info("Bidlist deleted successfuly, model updated");
        logger.info(bidList.toString());
        return "redirect:/bidList/list";
    }
}
