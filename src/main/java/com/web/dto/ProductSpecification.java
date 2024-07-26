package com.web.dto;

import com.web.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class ProductSpecification implements Specification<Product> {

    private List<Long> categoryIds;
    private List<Long> trademarkIds;
    private Double minPrice;
    private Double maxPrice;

    public ProductSpecification(List<Long> categoryIds, List<Long> trademarkIds, Double minPrice, Double maxPrice) {
        this.categoryIds = categoryIds;
        this.trademarkIds = trademarkIds;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (categoryIds != null && !categoryIds.isEmpty()) {
            predicate = cb.and(predicate, root.get("category").get("id").in(categoryIds));
        }

        if (trademarkIds != null && !trademarkIds.isEmpty()) {
            predicate = cb.and(predicate, root.get("trademark").get("id").in(trademarkIds));
        }

        if (minPrice != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        predicate = cb.and(predicate, cb.notEqual(root.get("deleted"), true));
        return predicate;
    }
}
