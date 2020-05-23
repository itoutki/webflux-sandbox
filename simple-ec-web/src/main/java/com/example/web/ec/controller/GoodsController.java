package com.example.web.ec.controller;

import com.example.web.ec.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    private GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    public String show(@RequestParam(name = "category", required = false, defaultValue = "0") int category,
                       Model model) {
        model.addAttribute("goodsForms", goodsService.findByCategoryId(category).stream().map(GoodsForm::new).collect(Collectors.toList()));
        return "goods";
    }

    @PostMapping
    public String add(GoodsForm goodsForm, Model model) {
        logger.info("request data id: {} name: {} price: {} quantity {}",
                goodsForm.getId(),
                goodsForm.getName(),
                goodsForm.getPrice(),
                goodsForm.getQuantity());
        return "redirect:/goods";
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("goodsForm", new GoodsForm(goodsService.findById(id)));
        return "goodsdetails";
    }
}
