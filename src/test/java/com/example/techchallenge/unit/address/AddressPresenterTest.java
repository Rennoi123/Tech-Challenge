package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.dto.AddressDTO;
import com.example.techchallenge.core.dto.AddressResponse;
import com.example.techchallenge.adapters.presenters.AddressPresenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressPresenterTest {

    @Test
    void deveConverterDomainParaResponse() {
        Address address = new Address(1L, "Rua A", "123", "Ap 1",
                "Centro", "Cidade X", "SP", "12345-000");

        AddressResponse response = AddressPresenter.toResponse(address);

        assertEquals(address.getId(), response.id());
        assertEquals("Rua A", response.street());
    }

    @Test
    void deveConverterDomainParaDTO() {
        Address address = new Address(2L, "Rua B", "456", null,
                "Bairro", "Cidade Y", "RJ", "54321-000");

        AddressDTO dto = AddressPresenter.toDTO(address);

        assertEquals("Rua B", dto.street());
        assertEquals("54321-000", dto.postalCode());
    }
}
