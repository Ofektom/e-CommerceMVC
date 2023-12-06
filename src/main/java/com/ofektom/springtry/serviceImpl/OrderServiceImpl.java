package com.ofektom.springtry.serviceImpl;

import com.ofektom.springtry.models.Order;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.repository.OrderRepositories;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.function.Function;

@Service
public class OrderServiceImpl {
    private OrderRepositories orderRepositories;
    private final UserServiceImpl userService;

    @Autowired
    public OrderServiceImpl(OrderRepositories orderRepositories, UserServiceImpl userService){
        this.orderRepositories = orderRepositories;
        this.userService = userService;
    }

    public Function<Order, Order> saveOrder = (order) ->orderRepositories.save(order);

    public String makePayment(HttpSession session, Model model){
        Users users = userService.findUserById.apply((Long)session.getAttribute("userID"));
        Order order  = (Order)session.getAttribute("order");
        if(users.getBalance().doubleValue()<order.getTotalPrice().doubleValue()){
            model.addAttribute("paid", "Insufficient balance!");
            return "checkout";
        }
        users.setBalance(users.getBalance().subtract(order.getTotalPrice()));
        userService.saveUser.apply(users);
        Order order1 = saveOrder.apply(order);
        session.setAttribute("order", null);
        model.addAttribute("paid", "Payment was successful");
        return "successfully-paid";
    }
}
