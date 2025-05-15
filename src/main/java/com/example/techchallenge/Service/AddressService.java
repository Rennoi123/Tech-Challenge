package com.example.techchallenge.Service;

import com.example.techchallenge.model.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public interface AddressService {
    AddressEntity getById(Long id);
}
