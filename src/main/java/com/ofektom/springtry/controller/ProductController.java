package com.ofektom.springtry.controller;


import com.ofektom.springtry.models.Product;
import com.ofektom.springtry.serviceImpl.OrderServiceImpl;
import com.ofektom.springtry.serviceImpl.ProductServiceImpl;
import com.ofektom.springtry.serviceImpl.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductServiceImpl productService;
    private UsersServiceImpl usersService;
    private OrderServiceImpl orderService;

    @Autowired
    public ProductController(ProductServiceImpl productService, UsersServiceImpl usersService, OrderServiceImpl orderService) {
        this.productService = productService;
        this.usersService = usersService;
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ModelAndView findAllProducts(HttpServletRequest request){
        HttpSession session = request.getSession();
        List<Product> productList = productService.findAllProducts.get();
        return new ModelAndView("dashboard")
                .addObject("products", productList)
                .addObject("cartItems", "Cart Items: "+session.getAttribute("cartItems"));
    }

    @GetMapping("/add-cart")
    public String addToCart(@RequestParam(name = "cart") Long id, HttpServletRequest request, Model model){
        productService.addProductToCart(id, request);
        return "redirect:/products/all";
    }

    @GetMapping("/restock")
    public String indexPage(Model model){
        model.addAttribute("product", new Product());
        return "admin-add-product";
    }



    @GetMapping("/payment")
    public String checkOut(HttpSession session, Model model){
        productService.checkOutCart(session, model);
        model.addAttribute("paid", "");
        return "checkout";
    }

    @GetMapping("/pay")
    public String orderPayment(HttpSession session, Model model){
        return orderService.makePayment(session, model);
    }

//    @PostMapping("/edit-product")
//    public String editProduct(@ModelAttribute Product product){
//        productService.editProduct.apply(product);
//        return "admin-add-product";
//    }
//
//    @PostMapping("/delete-product")
//    public String deleteProduct(@ModelAttribute Product product){
//        productService.deleteProduct.apply(product);
//        return "admin-add-product";
//    }

    @PostMapping("/add-product")
    public String addToProduct(@ModelAttribute Product product){
        productService.addNewProduct.apply(product);
        return "admin-add-product";
    }

}
