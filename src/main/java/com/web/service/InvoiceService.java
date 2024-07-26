package com.web.service;

import com.nimbusds.openid.connect.sdk.assurance.evidences.Voucher;
import com.web.config.Environment;
import com.web.dto.InvoiceRequest;
import com.web.entity.*;
import com.web.enums.PayType;
import com.web.enums.Status;
import com.web.exception.MessageException;
import com.web.models.QueryStatusTransactionResponse;
import com.web.processor.QueryTransactionStatus;
import com.web.repository.*;
import com.web.utils.CommonPage;
import com.web.utils.Contains;
import com.web.utils.MailService;
import com.web.utils.UserUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private HistoryPayRepository historyPayRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private InvoiceStatusRepository invoiceStatusRepository;

    @Autowired
    private CommonPage commonPage;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MailService mailService;

    
    public Invoice create(InvoiceRequest invoiceRequest) {
        if(invoiceRequest.getPayType().equals(PayType.PAYMENT_MOMO)){
            if(invoiceRequest.getRequestIdMomo() == null || invoiceRequest.getOrderIdMomo() == null){
                throw new MessageException("orderid and requestid require");
            }
            if(historyPayRepository.findByOrderIdAndRequestId(invoiceRequest.getOrderIdMomo(), invoiceRequest.getRequestIdMomo()).isPresent()){
                throw new MessageException("Đơn hàng đã được thanh toán");
            }
            Environment environment = Environment.selectEnv("dev");
            try {
                QueryStatusTransactionResponse queryStatusTransactionResponse = QueryTransactionStatus.process(environment, invoiceRequest.getOrderIdMomo(), invoiceRequest.getRequestIdMomo());
                System.out.println("qqqq-----------------------------------------------------------"+queryStatusTransactionResponse.getMessage());
                if(queryStatusTransactionResponse.getResultCode() != 0){
                    throw new MessageException("Đơn hàng chưa được thanh toán");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MessageException("Đơn hàng chưa được thanh toán");
            }
        }
        if(invoiceRequest.getUserAddressId() == null){
            throw new MessageException("user address id require");
        }
        Optional<UserAddress> userAddress = userAddressRepository.findById(invoiceRequest.getUserAddressId());
        if(userAddress.isEmpty()){
            throw new MessageException("user address not found");
        }
        if(userAddress.get().getUser().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("access deneid");
        }
        List<Cart> carts = cartRepository.findByUser(userUtils.getUserWithAuthority().getId());

        if(carts.size() == 0){
            throw new MessageException("Bạn chưa có sản phẩm nào trong giỏ hàng");
        }
        for(Cart c : carts){
            Size size = c.getSize();
            if (size.getQuantity() < c.getQuantity()){
                throw new MessageException("Sản phẩm "+c.getSize().getProduct().getName()+" chỉ còn lại "+c.getSize().getQuantity() +" sảm phẩm");
            }
        }
        Double totalAmount = 0D;
        for(Cart c : carts){
            Double price = c.getSize().getProduct().getPrice();
           if(c.getSize().getPrice() != null){
               if (c.getSize().getPrice() > 0){
                   price = c.getSize().getPrice();
               }
           }
           totalAmount += price * c.getQuantity();
        }


        Invoice invoice = new Invoice();
        invoice.setCreatedDate(new Date(System.currentTimeMillis()));
        invoice.setCreatedTime(new Time(System.currentTimeMillis()));
        invoice.setUserAddress(userAddress.get());
        invoice.setNote(invoiceRequest.getNote());
        invoice.setPhone(userAddress.get().getPhone());
        invoice.setAddress(userAddress.get().getStreetName()+", "+userAddress.get().getWards().getName()+", "+userAddress.get().getWards().getDistricts().getName()+". "+userAddress.get().getWards().getDistricts().getProvince().getName());
        invoice.setReceiverName(userAddress.get().getFullname());
        invoice.setPayType(invoiceRequest.getPayType());
        invoice.setStatus(Status.DANG_CHO_XAC_NHAN);
        invoice.setTotalAmount(totalAmount);
        Invoice result = invoiceRepository.save(invoice);

        for(Cart c : carts){
            Size productSize = c.getSize();
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoice(result);
            if(c.getSize().getPrice() != null){
                invoiceDetail.setPrice(c.getSize().getPrice());
            }
            else {
                invoiceDetail.setPrice(c.getSize().getProduct().getPrice());
            }
            invoiceDetail.setQuantity(c.getQuantity());
            invoiceDetail.setSize(productSize);
            invoiceDetailRepository.save(invoiceDetail);
            productSize.setQuantity(productSize.getQuantity() - c.getQuantity());
            sizeRepository.save(productSize);
            try {
                productSize.getProduct().setQuantitySold(productSize.getProduct().getQuantitySold() + c.getQuantity());
                productRepository.save(productSize.getProduct());
            }catch (Exception e){}
        }

        if(invoiceRequest.getPayType().equals(PayType.PAYMENT_MOMO)){
            HistoryPay historyPay = new HistoryPay();
            historyPay.setInvoice(result);
            historyPay.setRequestId(invoiceRequest.getRequestIdMomo());
            historyPay.setOrderId(invoiceRequest.getOrderIdMomo());
            historyPay.setCreatedTime(new Time(System.currentTimeMillis()));
            historyPay.setCreatedDate(new Date(System.currentTimeMillis()));
            historyPay.setTotalAmount(totalAmount);
            historyPayRepository.save(historyPay);
        }

        InvoiceStatus invoiceStatus = new InvoiceStatus();
        invoiceStatus.setInvoice(result);
        invoiceStatus.setCreatedDate(LocalDateTime.now());
        invoiceStatus.setStatus(Status.DANG_CHO_XAC_NHAN);
        invoiceStatus.setUser(userUtils.getUserWithAuthority());
        invoiceStatusRepository.save(invoiceStatus);

        return result;
    }

    
    public Invoice updateStatus(Long invoiceId, Status status) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if(invoice.isEmpty()){
            throw new MessageException("invoice id not found");
        }
        if(invoiceStatusRepository.findByInvoiceAndStatus(status,invoiceId).isPresent()){
            throw new MessageException("Trạng thái đơn hàng này đã được cập nhật");
        }
        InvoiceStatus invoiceStatus = new InvoiceStatus();
        invoiceStatus.setInvoice(invoice.get());
        invoiceStatus.setCreatedDate(LocalDateTime.now());
        invoiceStatus.setStatus(status);
        invoiceStatus.setUser(userUtils.getUserWithAuthority());
        invoiceStatusRepository.save(invoiceStatus);
        invoice.get().setStatus(status);
        String str = "";
        User user = userUtils.getUserWithAuthority();
        Date d = new Date(System.currentTimeMillis());
        Time t = new Time(System.currentTimeMillis());
        String time = t.toString() +" ngày "+d.toString();
        if (status.equals(Status.DA_GUI)){
            str = "đã được gửi đi";
        }
        if (status.equals(Status.DA_XAC_NHAN)){
            str = "đã được xác nhận";
        }
        if (status.equals(Status.KHONG_NHAN_HANG)){
            str = "đã được hủy vì bạn không nhận hàng";
        }
        if (status.equals(Status.DA_NHAN)){
            str = "đã xác nhận được giao thành công";
        }
        if (status.equals(Status.DA_HUY)){
            str = "đã được hủy";
        }
        mailService.sendEmail(invoice.get().getUserAddress().getUser().getEmail(), "Thông báo đơn hàng",
                "Đơn hàng: #"+invoice.get().getId()+" của bạn "+str+" bởi nhân viên "+user.getFullName() + " vào lúc "+time,
                false, true);
        return invoiceRepository.save(invoice.get());
    }

    
    public List<Invoice> findByUser() {
        User user = userUtils.getUserWithAuthority();
        List<Invoice> invoices = invoiceRepository.findByUser(user.getId());
        return invoices;
    }

    
    public Page<Invoice> findAll(Date from, Date to, Pageable pageable) {
        if(from == null || to == null){
            from = Date.valueOf("2000-01-01");
            to = Date.valueOf("2200-01-01");
        }
        Page<Invoice> page = invoiceRepository.findByDate(from, to,pageable);
        return page;
    }

    
    public Invoice cancelInvoice(Long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if(invoice.isEmpty()){
            throw new MessageException("invoice id not found");
        }
        if(invoice.get().getUserAddress().getUser().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("access denied");
        }
        if(invoice.get().getPayType().equals(PayType.PAYMENT_MOMO)){
            throw new MessageException("Đơn hàng đã được thanh toán, không thể hủy");
        }
        Status s = invoice.get().getStatus();
        if(s.equals(Status.DA_HUY) || s.equals(Status.DA_GUI) || s.equals(Status.DA_NHAN) || s.equals(Status.KHONG_NHAN_HANG)){
            throw new MessageException("không thể hủy hàng");
        }
        invoice.get().setStatus(Status.DA_HUY);
        Invoice result = invoiceRepository.save(invoice.get());

        List<InvoiceDetail> list  = invoiceDetailRepository.findByInvoiceId(invoiceId);
        for(InvoiceDetail i : list){
            i.getSize().setQuantity(i.getQuantity() + i.getSize().getQuantity());
            sizeRepository.save(i.getSize());
        }
        InvoiceStatus invoiceStatus = new InvoiceStatus();
        invoiceStatus.setInvoice(invoice.get());
        invoiceStatus.setCreatedDate(LocalDateTime.now());
        invoiceStatus.setStatus(Status.DA_HUY);
        invoiceStatusRepository.save(invoiceStatus);
        return result;
    }

    
    public Invoice findById(Long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if(invoice.isEmpty()){
            throw new MessageException("invoice id not found");
        }
        if(invoice.get().getUserAddress().getUser().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("access denied");
        }
        return invoice.get();
    }

    
    public Invoice findByIdForAdmin(Long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if(invoice.isEmpty()){
            throw new MessageException("invoice id not found");
        }
        return invoice.get();
    }

    
    public Page<Invoice> findAllFull(Date from, Date to, PayType payType, Status status, Pageable pageable) {
        if(from == null || to == null){
            from = Date.valueOf("2000-01-01");
            to = Date.valueOf("2200-01-01");
        }
        Page<Invoice> page = null;
        if(payType == null && status == null){
            page = invoiceRepository.findByDate(from, to,pageable);
        }
        if(payType == null && status != null){
            page = invoiceRepository.findByDateAndStatus(from, to, status,pageable);
        }
        if(payType != null && status == null){
            page = invoiceRepository.findByDateAndPaytype(from, to,payType,pageable);
        }
        if(payType != null && status != null){
            page = invoiceRepository.findByDateAndPaytypeAndStatus(from, to,payType,status,pageable);
        }

        return page;
    }
}
