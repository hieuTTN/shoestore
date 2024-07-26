package com.web.repository;

import com.web.entity.Product;
import com.web.entity.Trademark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrademarkRepository extends JpaRepository<Trademark, Long> {

}
