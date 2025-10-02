package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.usecase.address.ListAddressesUseCase;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAddressesUseCaseTest {

    @Mock
    private IAddressGateway gateway;

    @InjectMocks
    private ListAddressesUseCase useCase;

    @Test
    void deveListarEnderecos() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        when(gateway.findAll()).thenReturn(List.of(address));

        List<Address> result = useCase.execute();

        assertFalse(result.isEmpty());
        assertEquals("Rua A", result.get(0).getStreet());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistirEnderecos() {
        when(gateway.findAll()).thenReturn(Collections.emptyList());

        List<Address> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
