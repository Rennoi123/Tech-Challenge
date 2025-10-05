package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.usecase.address.ExistsAddressByIdUseCase;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExistsAddressByIdUseCaseTest {

    @Mock
    private IAddressGateway gateway;

    @InjectMocks
    private ExistsAddressByIdUseCase useCase;

    @Test
    void deveRetornarTrueSeEnderecoExistir() {
        when(gateway.existsById(1L)).thenReturn(true);
        assertTrue(useCase.execute(1L));
    }

    @Test
    void deveRetornarFalseSeEnderecoNaoExistir() {
        when(gateway.existsById(99L)).thenReturn(false);
        assertFalse(useCase.execute(99L));
    }
}
