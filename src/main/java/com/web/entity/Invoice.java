package com.web.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nimbusds.openid.connect.sdk.assurance.evidences.Voucher;
import com.web.enums.PayType;
import com.web.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice")
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Date createdDate;

    private Time createdTime;

    private Double totalAmount;

    private String receiverName;

    private String phone;

    private String address;

    private String note;

    private PayType payType;

    @ManyToOne
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

    private Status status;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<InvoiceStatus> invoiceStatuses = new ArrayList<>();
}
