package com.mechanic.cycleshop.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mechanic.cycleshop.entities.Cycle;
import com.mechanic.cycleshop.repositories.CyclesRepository;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private CyclesRepository shopRepository;

    @GetMapping("/restock")
    public String restock(Model model) {
        model.addAttribute("cycle", new Cycle());
        model.addAttribute("cycles", shopRepository.findAll());
        return "restock";
    }

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("cycles", shopRepository.findAllCyclesByQuantityGreaterThan(0));
        return "cycles";
    }

    @PostMapping("/add")
    public String addCycle(@ModelAttribute("cycle") Cycle cycle) {
        Cycle c = new Cycle();
        c.setBrand(cycle.getBrand());
        c.setColor(cycle.getColor());
        shopRepository.save(cycle);

        return "redirect:/shop/restock";
    }

    @PostMapping("/borrow/{id}")
    public String borrowCycle(@PathVariable int id) {
        Optional<Cycle> cycle = shopRepository.findById(id);

        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setQuantity(c.getQuantity() - 1);
            shopRepository.save(c);
        }

        return "redirect:/shop/list";
    }

    //fishy
    @PostMapping("/return/{id}")
    public String returnCycle(@PathVariable int id) {
        Optional<Cycle> cycle = shopRepository.findById(id);

        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setQuantity(c.getQuantity() + 1);
            shopRepository.save(c);
        }

        return "redirect:/shop/list";
    }

    @PostMapping("/restock/{id}")
    public String returnCycle(@PathVariable int id, @RequestParam("quantity") int quantity) {

        Optional<Cycle> cycle = shopRepository.findById(id);

        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setQuantity(c.getQuantity() + quantity);
            shopRepository.save(c);
        }

        return "redirect:/shop/restock";
    }
}
