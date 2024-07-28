package com.web.service;

import com.web.dto.ProductExcel;
import com.web.dto.ProductSpecification;
import com.web.elasticsearch.model.ProductSearch;
import com.web.elasticsearch.repository.ProductSearchRepository;
import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private TrademarkRepository trademarkRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    EntityManager em;

    
    public Product save(Product product) {
        product.setCreatedDate(LocalDateTime.now());
        product.setQuantitySold(0);
        product.setDeleted(false);
        Product result = productRepository.save(product);

        for(ProductImage p : product.getProductImages()){
            p.setProduct(result);
            productImageRepository.save(p);
        }

        for(Size size : product.getSizes()){
            size.setProduct(result);
            if (size.getPrice() == null){
                size.setPrice(result.getPrice());
            }
            sizeRepository.save(size);
        }
        return result;
    }

    
    public Product update(Product product) {
        Optional<Product> exist = productRepository.findById(product.getId());
        if(exist.isEmpty()){
            throw new MessageException("product not found");
        }
        product.setDeleted(false);
        product.setCreatedDate(exist.get().getCreatedDate());
        product.setQuantitySold(exist.get().getQuantitySold());
        Product result = productRepository.save(product);

        for(ProductImage p : product.getProductImages()){
            p.setProduct(result);
            productImageRepository.save(p);
        }

        for(Size size : product.getSizes()){
            size.setProduct(result);
            if (size.getPrice() == null){
                size.setPrice(result.getPrice());
            }
            sizeRepository.save(size);
        }
        return result;
    }

    
    public void delete(Long idProduct) {
        Optional<Product> exist = productRepository.findById(idProduct);
        if(exist.isEmpty()){
            throw new MessageException("product not found");
        }
        if(invoiceDetailRepository.countByProduct(idProduct) > 0){
            exist.get().setDeleted(true);
        }
        else{
            productRepository.deleteById(idProduct);
        }
    }

    public Product findById(Long id) {
        Optional<Product> exist = productRepository.findById(id);
        if(exist.isEmpty()){
            throw new MessageException("product not found");
        }
        return exist.get();
    }

    public List<Product> findAllList(){
        List<Product> list = productRepository.findAll();
        return list;
    }

    public Page<Product> findAllPage(Pageable pageable){
        Page<Product> page = productRepository.findAll(pageable);
        return page;
    }


    public Page<Product> findProductsByCriteria(List<Long> categoryIds, List<Long> trademarkIds, Double minPrice, Double maxPrice, Pageable pageable) {
        ProductSpecification spec = new ProductSpecification(categoryIds, trademarkIds, minPrice, maxPrice);
        return productRepository.findAll(spec, pageable);
    }

    public Page<Product> searchProduct(String search, Pageable pageable) {
        return productRepository.searchFull(search, pageable);
    }

    public List<ProductExcel> excelProduct(String type){
        List<ProductExcel> list = new ArrayList<>();
        if(type.equalsIgnoreCase("CATEGORY")){
            List<Category> categories = categoryRepository.findAll();
            for(Category c : categories){
                List<Product> products = productRepository.findByCategory(c.getId());
                ProductExcel cp = new ProductExcel();
                cp.setId(c.getId());
                cp.setName(c.getName());
                cp.setProducts(products);
                list.add(cp);
            }
        }
        if(type.equalsIgnoreCase("TRADEMARK")){
            List<Trademark> trademarks = trademarkRepository.findAll();
            for(Trademark c : trademarks){
                List<Product> products = productRepository.findByTrademark(c.getId());
                ProductExcel cp = new ProductExcel();
                cp.setId(c.getId());
                cp.setName(c.getName());
                cp.setProducts(products);
                list.add(cp);
            }
        }
        return list;
    }

    public Iterable<ProductSearch> findAll(){
        Iterable<ProductSearch> iterable = productSearchRepository.findAll();
        return iterable;
    }
}
