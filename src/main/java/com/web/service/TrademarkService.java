package com.web.service;

import com.web.entity.Category;
import com.web.entity.Trademark;
import com.web.exception.MessageException;
import com.web.repository.CategoryRepository;
import com.web.repository.TrademarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrademarkService {

    @Autowired
    private TrademarkRepository trademarkRepository;

    public Trademark save(Trademark trademark) {
        Trademark result = trademarkRepository.save(trademark);
        return result;
    }


    public void delete(Long trademarkId) {
        trademarkRepository.deleteById(trademarkId);
    }


    public Trademark findById(Long id) {
        return trademarkRepository.findById(id).get();
    }

    public List<Trademark> findAllList() {
        List<Trademark> list = trademarkRepository.findAll();
        return list;
    }
}
