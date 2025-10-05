package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.usecase.address.CreateOrUpdateAddressUseCase;
import com.example.techchallenge.core.dto.AddressDTO;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrUpdateAddressUseCaseTest {

    @Mock
    private IAddressGateway gateway;

    @InjectMocks
    private CreateOrUpdateAddressUseCase useCase;

    @Test
    void deveCriarOuAtualizarEndereco() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        when(gateway.save(any())).thenReturn(address);

        Address result = useCase.execute(address);

        assertEquals("Rua A", result.getStreet());
    }

    @Test
    void deveFalharSeGatewayLancarExcecao() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        when(gateway.save(any())).thenThrow(new RuntimeException("Erro ao salvar"));

        assertThrows(RuntimeException.class, () -> useCase.execute(address));
    }
}
