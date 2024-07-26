package com.web.api;

import com.web.entity.ImportProduct;
import com.web.service.ImportProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/import-product")
@CrossOrigin
public class ImportProductApi {

    @Autowired
    private ImportProductService importProductService;

    @PostMapping("/admin/create")
    public ResponseEntity<?> create(@RequestBody ImportProduct importProduct){
        ImportProduct result = importProductService.create(importProduct);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<?> update(@RequestBody ImportProduct importProduct){
        ImportProduct result = importProductService.update(importProduct);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> update(@RequestParam("id") Long id){
        importProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<?> findAll(Pageable pageable){
        Page<ImportProduct> importProducts = importProductService.getAll(pageable);
        return new ResponseEntity<>(importProducts,HttpStatus.OK);
    }

    @GetMapping("/admin/findByProductAndDate")
    public ResponseEntity<?> findAll(@RequestParam(value = "idproduct", required = false) Long idProduct,
                                     @RequestParam(value = "from", required = false) Date from,
                                     @RequestParam(value = "to", required = false) Date to){
        List<ImportProduct> importProducts = importProductService.getByProductAndDate(idProduct,from,to);
        return new ResponseEntity<>(importProducts,HttpStatus.OK);
    }

    @GetMapping("/admin/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
        ImportProduct result = importProductService.findById(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
