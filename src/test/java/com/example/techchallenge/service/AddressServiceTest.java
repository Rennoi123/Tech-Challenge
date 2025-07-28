package com.example.techchallenge.service;

import com.example.techchallenge.dto.Request.AddressRequest;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private AddressRequest requisicaoEndereco;
    private AddressEntity entidadeEndereco;

    @BeforeEach
    void setUp() {
        requisicaoEndereco = new AddressRequest(
                1L, "Rua das Flores", "10", "Apto 101", "Jardim", "S�o Paulo", "SP", "01234-567"
        );
        entidadeEndereco = new AddressEntity();
        entidadeEndereco.setId(1L);
        entidadeEndereco.setStreet("Rua das Flores");
        entidadeEndereco.setNumber("10");
        entidadeEndereco.setComplement("Apto 101");
        entidadeEndereco.setNeighborhood("Jardim");
        entidadeEndereco.setCity("S�o Paulo");
        entidadeEndereco.setState("SP");
        entidadeEndereco.setPostalCode("01234-567");
    }

    @Test
    @DisplayName("Deve criar um novo endereço se o ID for nulo")
    void deveCriarNovoEnderecoSeIdForNulo() {
        AddressRequest novaRequisicao = new AddressRequest(
                null, "Rua Nova", "1", null, "Centro", "Rio", "RJ", "20000-000"
        );
        AddressEntity novaEntidade = new AddressEntity();
        novaEntidade.setStreet("Rua Nova");

        when(addressRepository.save(any(AddressEntity.class))).thenReturn(novaEntidade);

        AddressEntity resultado = addressService.createOrUpdateAddress(novaRequisicao);

        assertNotNull(resultado);
        assertEquals(novaRequisicao.street(), resultado.getStreet());
        verify(addressRepository, times(1)).save(any(AddressEntity.class));
        verify(addressRepository, never()).existsById(anyLong());
    }

    @Test
    @DisplayName("Deve atualizar um endereço existente se o ID existir")
    void deveAtualizarEnderecoSeIdExistir() {
        when(addressRepository.existsById(anyLong())).thenReturn(true);
        when(addressRepository.save(any(AddressEntity.class))).thenReturn(entidadeEndereco);

        AddressEntity resultado = addressService.createOrUpdateAddress(requisicaoEndereco);

        assertNotNull(resultado);
        assertEquals(requisicaoEndereco.id(), resultado.getId());
        assertEquals(requisicaoEndereco.street(), resultado.getStreet());
        verify(addressRepository, times(1)).existsById(requisicaoEndereco.id());
        verify(addressRepository, times(1)).save(any(AddressEntity.class));
    }

    @Test
    @DisplayName("Deve retornar o endereço pelo ID")
    void deveRetornarEnderecoPorId() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(entidadeEndereco));

        AddressEntity resultado = addressService.getById(1L);

        assertNotNull(resultado);
        assertEquals(entidadeEndereco.getId(), resultado.getId());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o endereço não for encontrado pelo ID")
    void deveLancarExcecaoQuandoEnderecoNaoEncontradoPorId() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> {
            addressService.getById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertTrue(excecao.getReason().contains("Endereço não encontrado pelo id: 99"));
        verify(addressRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro se o endereço existir pelo ID")
    void deveRetornarVerdadeiroSeEnderecoExistirPorId() {
        when(addressRepository.existsById(anyLong())).thenReturn(true);

        assertTrue(addressService.existsById(1L));
        verify(addressRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Deve retornar falso se o endereço não existir pelo ID")
    void deveRetornarFalsoSeEnderecoNaoExistirPorId() {
        when(addressRepository.existsById(anyLong())).thenReturn(false);

        assertFalse(addressService.existsById(99L));
        verify(addressRepository, times(1)).existsById(99L);
    }
}
