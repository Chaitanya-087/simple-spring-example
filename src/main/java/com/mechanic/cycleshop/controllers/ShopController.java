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

import com.mechanic.cycleshop.entities.Cycle;
import com.mechanic.cycleshop.repositories.CyclesRepository;


@Controller
@RequestMapping("/shop")
public class ShopController {
    
    @Autowired
    private CyclesRepository shopRepository;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("cycles", shopRepository.findAll());
        return "cycles";
    }

    @GetMapping("/cycle/form")
    public String addCycle(Model model) {
        model.addAttribute("cycle", new Cycle());
        return "addForm";
    }

    @PostMapping("/cycle/add")
    public String addCycle(@ModelAttribute("cycle") Cycle cycle) {
        Cycle c = new Cycle();
        c.setBrand(cycle.getBrand());
        c.setColor(cycle.getColor());
        shopRepository.save(cycle);

        return "redirect:/shop/showAllAvailableCycles";
    }

    @GetMapping("/showAllAvailableCycles")
    public String showAvailableCycles(Model model) {
        model.addAttribute("cycles", shopRepository.findByAvailable(true));
        return "cycles";
    }

    @GetMapping("/borrowCycle/{id}")
    public String borrowCycle(@PathVariable int id) {
        Optional<Cycle> cycle = shopRepository.findById(id);
        
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setAvailable(false);
            shopRepository.save(c);
        }

        return "redirect:/shop/showAllAvailableCycles";
    }

    @GetMapping("/returnCycle/{id}")
    public String returnCycle(@PathVariable int id) {

        Optional<Cycle> cycle = shopRepository.findById(id);

        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setAvailable(true);
            shopRepository.save(c);
        }
        
        return "redirect:/shop/showAllAvailableCycles";
    }
}
