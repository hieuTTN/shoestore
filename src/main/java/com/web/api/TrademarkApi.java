package com.web.api;

import com.web.entity.Category;
import com.web.entity.Trademark;
import com.web.repository.CategoryRepository;
import com.web.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trademark")
@CrossOrigin
public class TrademarkApi {

    @Autowired
    private TrademarkService trademarkService;

    @GetMapping("/public/all-trademark")
    public ResponseEntity<?> findAllCategory() {
        List<Trademark> result = trademarkService.findAllList();
        return new ResponseEntity(result, HttpStatus.OK);
    }


    @PostMapping("/admin/create")
    public ResponseEntity<?> save(@RequestBody Trademark trademark){
        Trademark result = trademarkService.save(trademark);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
        trademarkService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
        Trademark result = trademarkService.findById(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
