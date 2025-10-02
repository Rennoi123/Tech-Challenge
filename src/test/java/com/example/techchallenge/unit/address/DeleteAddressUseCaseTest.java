package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.usecase.address.DeleteAddressUseCase;
import com.example.techchallenge.core.dto.AddressDTO;
import com.example.techchallenge.core.exception.AddressNotFoundException;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteAddressUseCaseTest {

    @Mock
    private IAddressGateway gateway;

    @InjectMocks
    private DeleteAddressUseCase useCase;

    @Test
    void deveDeletarEndereco() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        when(gateway.findById(1L)).thenReturn(Optional.of(address));

        assertDoesNotThrow(() -> useCase.execute(address.getId()));

        verify(gateway, times(1)).delete(address);
    }

    @Test
    void deveFalharQuandoGatewayLancarExcecao() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        when(gateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> useCase.execute(address.getId()));

        verify(gateway, never()).delete(any());
    }
}
