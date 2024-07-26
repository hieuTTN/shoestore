package com.web.service;

import com.web.entity.ImportProduct;
import com.web.entity.Size;
import com.web.exception.MessageException;
import com.web.repository.ImportProductRepository;
import com.web.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ImportProductService {

    @Autowired
    private ImportProductRepository importProductRepository;

    @Autowired
    private SizeRepository sizeRepository;

    
    public ImportProduct create(ImportProduct importProduct) {
        if(importProduct.getId() != null){
            throw new MessageException("id must null");
        }
        Optional<Size> productSize = sizeRepository.findById(importProduct.getSize().getId());
        if(productSize.isEmpty()){
            throw new MessageException("product size not found");
        }
        importProduct.setImportDate(new Date(System.currentTimeMillis()));
        importProduct.setImportTime(new Time(System.currentTimeMillis()));
        ImportProduct result = importProductRepository.save(importProduct);
        productSize.get().setQuantity(result.getQuantity() + productSize.get().getQuantity());
        sizeRepository.save(productSize.get());
        return result;
    }

    
    public ImportProduct update(ImportProduct importProduct) {
        if(importProduct.getId() == null){
            throw new MessageException("id require");
        }
        Optional<ImportProduct> exist = importProductRepository.findById(importProduct.getId());
        if(exist.isEmpty()){
            throw new MessageException("not found");
        }

        Size size = exist.get().getSize();
        size.setQuantity(size.getQuantity() - exist.get().getQuantity());
        if(size.getId() != importProduct.getSize().getId()){
            if (size.getQuantity() < 0){
                throw new MessageException("Số lượng sản phẩm của size "+size.getName()+" sản phẩm "+size.getProduct().getName()+" < 0");
            }
        }

        Optional<Size> sz = sizeRepository.findById(importProduct.getSize().getId());
        sz.get().setQuantity(sz.get().getQuantity() + importProduct.getQuantity());
        if (sz.get().getQuantity() < 0){
            throw new MessageException("Số lượng sản phẩm của size "+size.getName()+" sản phẩm "+size.getProduct().getName()+" < 0");
        }

        sizeRepository.save(size);
        sizeRepository.save(sz.get());

        importProduct.setImportDate(exist.get().getImportDate());
        importProduct.setImportTime(exist.get().getImportTime());
        ImportProduct result = importProductRepository.save(importProduct);
        return result;
    }

    
    public void delete(Long id) {
        Optional<ImportProduct> exist = importProductRepository.findById(id);
        if(exist.isEmpty()){
            throw new MessageException("not found");
        }
        Size productSize = exist.get().getSize();;
        productSize.setQuantity(productSize.getQuantity() - exist.get().getQuantity());
        if(productSize.getQuantity() < 0){
            throw new MessageException("Quantity of product size < 0 ");
        }
        sizeRepository.save(productSize);
        importProductRepository.delete(exist.get());
    }

    
    public ImportProduct findById(Long id) { Optional<ImportProduct> exist = importProductRepository.findById(id);
        if(exist.isEmpty()){
            throw new MessageException("not found");
        }
        return exist.get();
    }

    
    public Page<ImportProduct> getAll(Pageable pageable) {
        Page<ImportProduct> page = importProductRepository.findAll(pageable);
        return page;
    }

    
    public List<ImportProduct> getByProductAndDate(Long productId, Date from, Date to) {
        if(from == null || to == null){
            from = Date.valueOf("2000-01-01");
            to = Date.valueOf("2200-01-01");
        }
        System.out.println("==== from: "+from);
        System.out.println("==== to: "+to);
        List<ImportProduct> importProducts = null;
        if(productId == null){
            importProducts = importProductRepository.findByDate(from,to);
        }
        else{
            importProducts = importProductRepository.findByDateAndProduct(from,to,productId);
        }
        return importProducts;
    }
}
