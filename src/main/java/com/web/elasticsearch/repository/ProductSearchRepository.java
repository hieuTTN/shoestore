package com.web.elasticsearch.repository;

import com.web.elasticsearch.model.ProductSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, Long> {

}
