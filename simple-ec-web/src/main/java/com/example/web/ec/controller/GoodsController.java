package com.example.web.ec.controller;

import com.example.web.ec.model.CartItem;
import com.example.web.ec.model.Goods;
import com.example.web.ec.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    private GoodsService goodsService;
    private AccountForm accountForm;
    private CartForm cartForm;

    public GoodsController(GoodsService goodsService,
                           AccountForm accountForm,
                           CartForm cartForm) {
        this.goodsService = goodsService;
        this.accountForm = accountForm;
        this.cartForm = cartForm;
    }

    @GetMapping
    public String show(@RequestParam(name = "category", required = false, defaultValue = "0") int category,
                       Model model) {
        model.addAttribute("account", accountForm.getAccount());
        model.addAttribute("goodsForms", goodsService
                .findByCategoryId(category)
                .stream()
                .map(GoodsForm::new)
                .collect(Collectors.toList()));
        model.addAttribute("cart", cartForm.getCart());
        return "goods";
    }

    @PostMapping("/add")
    public String addToCart(@Validated GoodsForm goodsForm, BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        logger.info("request data id: {} name: {} price: {} quantity {}",
                goodsForm.getId(),
                goodsForm.getName(),
                goodsForm.getPrice(),
                goodsForm.getQuantity());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("quantity_error", "数量は1以上を指定してください");
            return "redirect:/goods";
        }
        Goods goods = goodsService.findById(goodsForm.getId());
        CartItem cartItem = new CartItem();
        cartItem.setGoods(goods);
        cartItem.setQuantity(goodsForm.getQuantity());
        cartForm.getCart().add(cartItem);
        return "redirect:/goods";
    }

    @PostMapping("/remove")
    public String removeFromCart(String id, Model model) {
        logger.info("goods id : {}", id);
        cartForm.getCart().remove(Set.of(id));
        return "redirect:/goods";
    }

    @PostMapping("/clear")
    public String clearCart(Model model) {
        cartForm.getCart().clear();
        return "redirect:/goods";
    }


    @GetMapping("/{id}")
    public String showDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("goodsForm", new GoodsForm(goodsService.findById(id)));
        return "goodsdetails";
    }
}
