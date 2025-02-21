package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.greenwhite.webstore.entity.Orders;
import uz.greenwhite.webstore.enums.OrderStatus;
import uz.greenwhite.webstore.service.OrderService;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/orders")
public class OrdersController {

    private final OrderService service;

    @GetMapping()
    public String listPage(Model model, Pageable pageable) {
        model.addAttribute("orders", service.getAll(pageable));
        return "/admin/data/orders/list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("orders", new Orders());
        return "/admin/data/orders/add";
    }

    @PostMapping("/add")
    public String addOrder(@ModelAttribute Orders orders) {
        service.save(orders);
        return "redirect:/admin/data/orders";
    }

    @GetMapping("/edit")
    public String editPage(Model model, @RequestParam("id") Long id) {
        model.addAttribute("orders", service.getById(id));
        return "/admin/data/orders/add";
    }

    @PostMapping("/edit")
    public String editOrder(@ModelAttribute Orders orders) {
        service.update(orders);
        return "redirect:/admin/data/orders";
    }

    @GetMapping("delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
            service.deleteById(id);
        return "redirect:/admin/data/orders";
    }
}