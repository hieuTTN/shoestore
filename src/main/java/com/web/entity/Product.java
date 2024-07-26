package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "trademark_id")
    private Trademark trademark;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Size> sizes;

}
