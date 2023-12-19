package com.ofektom.springtry.serviceImpl;

import com.ofektom.springtry.models.Cart;
import com.ofektom.springtry.models.Order;
import com.ofektom.springtry.models.Product;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.repository.ProductRepositories;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class ProductServiceImpl {

    private ProductRepositories productRepositories;

    @Autowired
    public ProductServiceImpl(ProductRepositories productRepositories) {
        this.productRepositories = productRepositories;
    }

    public Supplier<List<Product>> findAllProducts = () -> productRepositories.findAll();
    public Function<Long, Product> findById = (id) ->
            productRepositories.findById(id)
                    .orElseThrow(() ->
                            new NullPointerException("No such product found with ID: " + id));

    public void addNewProduct(String category, BigDecimal price, String productName, Long quantity) {
        // Create a new product with the provided details
        Product newProduct = Product.builder()
                .categories(category)
                .price(price)
                .productName(productName)
                .quantity(quantity)
                // You might set other properties here
                .build();

        // Save the new product to the repository or perform other necessary actions
        productRepositories.save(newProduct);
    }
//    public Function<Product, Product> addNewProduct = (product)->productRepositories.save(product);

    public void addProductToCart(Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart;
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
            cart.setProductIds(cart.getProductIds() + "," + id);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        } else {
            cart = Cart.builder().productIds(id.toString())
                    .userId((Long) session.getAttribute("userID")).build();
            session.setAttribute("cart", cart);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        }
    }

    public void checkOutCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        List<Product> productList = new ArrayList<>();
        List<String> productIds = Arrays.stream(cart.getProductIds().split(",")).toList();
        productIds.forEach(id -> {
            productList.add(productRepositories.findById(Long.parseLong(id)).orElseThrow(() ->
                    new NullPointerException("No such product found with ID: " + id)));
        });

        final BigDecimal[] totalPrice = {new BigDecimal(0)};
        productList.forEach(product -> totalPrice[0] = totalPrice[0].add(product.getPrice()));
        model.addAttribute("totalPrice", "Total Price: $" + totalPrice[0]);
        session.setAttribute("cart", null);
        Order order = Order.builder()
                .productList(productList)
                .userId((Long) session.getAttribute("userID"))
                .totalPrice(totalPrice[0])
                .build();
        session.setAttribute("order", order);
        model.addAttribute("order", order);
    }

    public Product getProductById(Long id) {
        return productRepositories.findById(id).orElseThrow(() -> new NullPointerException("No such product found with ID: " + id));
    }


    public void deleteProductById(Long id) {
        productRepositories.deleteById(id);
    }

    public List<Product> getProductByName(String name){
        return productRepositories.findByProductName(name);
    }

}
