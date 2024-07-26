package com.web.service;

import com.web.entity.Product;
import com.web.entity.ProductImage;
import com.web.entity.Size;
import com.web.exception.MessageException;
import com.web.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public Size update(Size size) {
        Optional<Size> exist = sizeRepository.findById(size.getId());
        size.setProduct(exist.get().getProduct());
        return sizeRepository.save(size);
    }

}
