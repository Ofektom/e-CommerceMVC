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

import java.math.BigDecimal;
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
    public ModelAndView findAllProducts(){
        List<Product> productList = productService.findAllProducts.get();
        return new ModelAndView("dashboard")
                .addObject("products", productList)
                .addObject("cartItems", "");
    }

    @GetMapping("/all-cart")
    public ModelAndView viewAllProductsAndCarts(HttpServletRequest request){
        HttpSession session = request.getSession();
        List<Product> productList = productService.findAllProducts.get();
        return new ModelAndView("dashboard")
                .addObject("products", productList)
                .addObject("cartItems", "Cart Items: " + session.getAttribute("cartItems"));
    }
    @GetMapping("/add-cart")
    public String addToCart(@RequestParam(name = "cart") Long id, HttpServletRequest request, Model model){
        productService.addProductToCart(id, request);
        return "redirect:/products/all-cart";
    }

    @GetMapping("/noCart")
    public String noCart(){
        return "cart-redirect";
    }

    @GetMapping("/payment")
    public String checkOut(HttpSession session, Model model){
        if(session.getAttribute("userID")!=null){
            productService.checkOutCart(session, model);
            model.addAttribute("paid", "");
            return "checkout";
        }
        return "redirect:/user/login-payment";
    }

    @GetMapping("/pay")
    public String orderPayment(HttpSession session, Model model){
        return orderService.makePayment(session, model);
    }


@PostMapping("/add-product")
public String addNewProductToStore(
        @RequestParam(name = "category") String category,
        @RequestParam(name = "price") BigDecimal price,
        @RequestParam(name = "productName") String productName,
        @RequestParam(name = "quantity") Long quantity) {
    productService.addNewProduct(category, price, productName, quantity);
    return "redirect:/products/all";
}

    @GetMapping("/restock")
    public String addProduct(Model model){
        model.addAttribute("products", new Product());
        return "admin-add-product";
    }

    @GetMapping("/edit-product/{id}")
    public String editPage(@PathVariable(value = "id")Long id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("products", product);
        return "admin-edit-product";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProductById(@PathVariable(value = "id")Long id){
        productService.deleteProductById(id);
        return "redirect:/products/all";
    }

    @GetMapping("/search-product/{productName}")
    public String searchProductByName(@PathVariable(value = "productName")String productName, Model model){
        List<Product> productList = productService.getProductByName(productName);
        if(!productList.isEmpty()){
            model.addAttribute("searchProducts", productList);
            return "search-product";
        }
        else{
            model.addAttribute("productNotFound", true);
            return "redirect:/products/search-product";
        }
    }

}
