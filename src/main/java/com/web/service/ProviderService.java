package com.web.service;

import com.web.entity.Provider;
import com.web.exception.MessageException;
import com.web.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    public Provider save(Provider provider) {
        Provider result = providerRepository.save(provider);
        return result;
    }

    public void delete(Long departmentId) {
        providerRepository.deleteById(departmentId);
    }

    public Provider findById(Long id) {
        Optional<Provider> provider = providerRepository.findById(id);
        if(provider.isEmpty()){
            throw new MessageException("Not found provider :"+id);
        }
        return provider.get();
    }

    public List<Provider> findAll() {
        List<Provider> providers = providerRepository.findAll();
        return providers;
    }


}
