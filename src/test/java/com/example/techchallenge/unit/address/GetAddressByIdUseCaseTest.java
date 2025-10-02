package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.usecase.address.GetAddressByIdUseCase;
import com.example.techchallenge.core.exception.AddressNotFoundException;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAddressByIdUseCaseTest {

    @Mock
    private IAddressGateway gateway;

    @InjectMocks
    private GetAddressByIdUseCase useCase;

    @Test
    void deveBuscarEnderecoPorId() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        when(gateway.findById(1L)).thenReturn(Optional.of(address));

        Address result = useCase.execute(1L);

        assertEquals("Rua A", result.getStreet());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarEndereco() {
        when(gateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> useCase.execute(1L));
    }
}
