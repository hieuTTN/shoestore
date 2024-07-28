package com.web.elasticsearch.model;

import com.web.entity.Category;
import com.web.entity.ProductImage;
import com.web.entity.Size;
import com.web.entity.Trademark;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "product")
@Getter
@Setter
public class ProductSearch {

    @Id
    private Long id;

    private String code;

    private String name;

    private String imageBanner;

    private Double oldPrice;

    private Double price;

    private String description;

    private String instructionSize;

    private LocalDateTime createdDate;

    private Integer quantitySold;

    private Boolean deleted = false;

    private Category category;

    private Trademark trademark;

    private List<ProductImage> productImages;

    private List<Size> sizes;
}
