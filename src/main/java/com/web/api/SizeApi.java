package com.web.api;

import com.web.entity.Product;
import com.web.entity.Size;
import com.web.repository.SizeRepository;
import com.web.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/size")
@CrossOrigin
public class SizeApi {

    @Autowired
    private SizeService sizeService;

    @Autowired
    SizeRepository sizeRepository;

    @PostMapping("/admin/update")
    public ResponseEntity<?> update(@RequestBody Size size){
        Size response = sizeService.update(size);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
        sizeRepository.deleteById(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    @GetMapping("/public/find-by-product")
    public ResponseEntity<?> findAllList(@RequestParam Long idProduct){
        List<Size> response = sizeRepository.findByProduct(idProduct);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
