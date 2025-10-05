package com.example.techchallenge.unit.address;

import com.example.techchallenge.adapters.gateways.AddressGatewayAdapter;
import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import com.example.techchallenge.infrastructure.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressGatewayAdapterTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressGatewayAdapter adapter;

    @Test
    void deveSalvarEndereco() {
        Address address = new Address(null, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        AddressEntity entity = AddressEntity.fromDomain(address);
        entity.setId(1L);

        when(addressRepository.save(any())).thenReturn(entity);

        Address saved = adapter.save(address);

        assertNotNull(saved.getId());
        assertEquals("Rua A", saved.getStreet());
    }

    @Test
    void deveBuscarPorId() {
        AddressEntity entity = new AddressEntity();
        entity.setId(1L);
        entity.setStreet("Rua A");

        when(addressRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Address> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Rua A", result.get().getStreet());
    }

    @Test
    void deveRetornarEmptyQuandoNaoEncontrar() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Address> result = adapter.findById(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void deveListarEnderecos() {
        AddressEntity entity = new AddressEntity();
        entity.setId(1L);
        entity.setStreet("Rua A");

        when(addressRepository.findAll()).thenReturn(List.of(entity));

        List<Address> result = adapter.findAll();

        assertFalse(result.isEmpty());
    }
}
