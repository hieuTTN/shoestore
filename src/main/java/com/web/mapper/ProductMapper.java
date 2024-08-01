package com.web.mapper;

import com.web.elasticsearch.model.ImageProductSearch;
import com.web.elasticsearch.model.ProductSearch;
import com.web.entity.Category;
import com.web.entity.Product;
import com.web.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private ModelMapper mapper;

    public ProductSearch productToProductSearch(Product product){
        ProductSearch response = mapper.map(product, ProductSearch.class);
        response.getCategory().setProducts(null);
        List<ImageProductSearch> list = new ArrayList<>();
        product.getProductImages().forEach(p->{
            ImageProductSearch imageProductSearch = new ImageProductSearch();
            imageProductSearch.setLinkImage(p.getLinkImage());
            imageProductSearch.setId(p.getId());
            list.add(imageProductSearch);
        });

        response.setProductImages(list);
        return response;
    }

}
