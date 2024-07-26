package com.web.api;

import com.web.entity.Provider;
import com.web.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ProviderApi {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/admin/findAll")
    public ResponseEntity<?> findAll(){
        List<Provider> result = providerService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/employee/findAll")
    public ResponseEntity<?> findAllByEmp(){
        List<Provider> result = providerService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> save(@RequestBody Provider provider){
        Provider result = providerService.save(provider);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
        providerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
        Provider result = providerService.findById(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}

