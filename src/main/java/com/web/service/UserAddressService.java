package com.web.service;

import com.web.entity.User;
import com.web.entity.UserAddress;
import com.web.exception.MessageException;
import com.web.repository.InvoiceRepository;
import com.web.repository.UserAddressRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Component
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserUtils userUtils;


    public List<UserAddress> findByUser() {
        User user = userUtils.getUserWithAuthority();
        List<UserAddress> userAddresses = userAddressRepository.findByUser(user.getId());
        return userAddresses;
    }

    
    public UserAddress findById(Long id) {
        Optional<UserAddress> userAddress = userAddressRepository.findById(id);
        if(userAddress.isEmpty()){
            throw new MessageException("user address not found");
        }
        if(userUtils.getUserWithAuthority().getId() != userAddress.get().getUser().getId()){
            throw new MessageException("access denied");
        }
        return userAddress.get();
    }

    
    public UserAddress create(UserAddress userAddress) {
        User user = userUtils.getUserWithAuthority();
        userAddress.setUser(user);
        userAddress.setCreatedDate(new Date(System.currentTimeMillis()));
        if(userAddress.getPrimaryAddres() == true){
            userAddressRepository.unSetPrimary(user.getId());
        }
        UserAddress result = userAddressRepository.save(userAddress);
        return result;
    }

    
    public void delete(Long id) {
        Optional<UserAddress> userAddress = userAddressRepository.findById(id);
        if(userAddress.isEmpty()){
            throw new MessageException("user address not found");
        }
        if(userUtils.getUserWithAuthority().getId() != userAddress.get().getUser().getId()){
            throw new MessageException("access denied");
        }
        invoiceRepository.setNull(id);
        userAddressRepository.deleteById(id);
    }
}
