package com.web.api;

import com.web.entity.Banner;
import com.web.entity.Cart;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartApi {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/my-cart")
    public ResponseEntity<?> myCart(){
        List<Cart> result = cartService.findByUser();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/user/create")
    public ResponseEntity<?> add(@RequestParam("sizeId") Long sizeId, @RequestParam Integer quantity){
        cartService.addCart(sizeId, quantity);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteById(@RequestParam("id") Long id){
        cartService.remove(id);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    @DeleteMapping("/user/delete-all")
    public ResponseEntity<?> delete(){
        cartService.removeCart();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/up-cart")
    public ResponseEntity<?> upCart(@RequestParam("id") Long id){
        cartService.upQuantity(id);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    @GetMapping("/user/down-cart")
    public ResponseEntity<?> downCart(@RequestParam("id") Long id){
        cartService.downQuantity(id);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    @GetMapping("/user/count-cart")
    public ResponseEntity<?> countCart(){
        Long count = cartService.countCart();
        return new ResponseEntity<>(count,HttpStatus.OK);
    }
}
