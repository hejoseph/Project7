package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeController {
    private static final Logger logger = LogManager.getLogger(TradeController.class);
    public LocalDateTime localDateTime;

    @Autowired
    TradeRepository tradeRepository;

    /**
     * This endpoint allows to show trade' list
     * @param model is the public interface model, attributes can be added, model can be accessed
     * @return the trade' list
     */
    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", tradeRepository.findAll());
        logger.info("Display of trade list successful");
        return "trade/list";
    }

    /**
     * This endpoint allows to display trade' adding form
     */
    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    /**
     * To validate data "Trade", then save it to db
     * @param trade
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            localDateTime = LocalDateTime.now();
            trade.setCreationDate(localDateTime);
            trade.setTradeDate(localDateTime);
            tradeRepository.save(trade);
            model.addAttribute("trades", tradeRepository.findAll());
            logger.info("Trade valid and added");
            logger.info(trade.toString());
            return "redirect:/trade/list";
        }
        logger.info("Error on trade validation, go back to adding form");
        logger.info(trade.toString());
        return "trade/add";
    }


    /**
     * show update form with "trade" data, from its id
     * @param id trade id to show
     * @param model to store value and can be accessed from the front
     * @return
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        model.addAttribute("trade", trade);
        logger.info("Trade's updating form display successful");
        logger.info(trade.toString());
        return "trade/update";
    }

    /**
     * update trade data by id
     * @param id trade id
     * @param trade trade with new data captured
     * @param result if data is valid
     * @param model
     * @return is the public interface model, attributes can be added, model can be accessed
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            logger.info("Trade updating failed, go back to updating form display, has errors");
            logger.info(trade.toString());
            return "trade/update";
        }
        trade.setId(id);
        tradeRepository.save(trade);
        model.addAttribute("trades", tradeRepository.findAll());
        logger.info("Trade saved successfully, model updated");
        logger.info(trade.toString());
        return "redirect:/trade/list";
    }

    /**
     * delete trade data by id
     * @param id trade
     * @param model data stored and accessed in the front
     * @return
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeRepository.delete(trade);
        model.addAttribute("trades", tradeRepository.findAll());
        logger.info("Trade deleted successfully, model updated");
        logger.info(trade.toString());
        return "redirect:/trade/list";
    }
}
