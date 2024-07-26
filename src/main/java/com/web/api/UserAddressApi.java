package com.web.api;

import com.web.entity.UserAddress;
import com.web.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user-address")
@CrossOrigin
public class UserAddressApi {

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping("/user/create-update")
    public ResponseEntity<?> save(@RequestBody UserAddress userAddress){
        UserAddress result = userAddressService.create(userAddress);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
        userAddressService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/my-address")
    public ResponseEntity<?> findAll(){
        List<UserAddress> result = userAddressService.findByUser();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/user/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
        UserAddress result = userAddressService.findById(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
