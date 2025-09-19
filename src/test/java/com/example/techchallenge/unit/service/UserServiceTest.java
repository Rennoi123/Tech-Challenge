package com.example.techchallenge.unit.service;

import com.example.techchallenge.dto.Request.UserRequest;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.exception.InvalidCredentialsException;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.repository.UserRepository;
import com.example.techchallenge.service.AddressService;
import com.example.techchallenge.service.RestaurantService;
import com.example.techchallenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AddressService addressService;
    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private UserEntity userEntity;
    private AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        var addressRequest = new com.example.techchallenge.dto.Request.AddressRequest(
            null, "Rua Teste", "123", "Apto 1", "Bairro Teste", "Cidade Teste", "ST", "12345678"
        );

        userRequest = new UserRequest(
            null, "Test User", "test@example.com", "password123", addressRequest, UserRoles.CLIENTE
        );

        addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua Teste");
        addressEntity.setNumber("123");
        addressEntity.setNeighborhood("Bairro Teste");
        addressEntity.setCity("Cidade Teste");
        addressEntity.setState("ST");
        addressEntity.setPostalCode("12345678");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setRoles(UserRoles.CLIENTE);
        userEntity.setAddress(addressEntity);
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void deveCriarNovoUsuarioComSucesso() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(addressService.createOrUpdateAddress(any())).thenReturn(addressEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity createdUser = userService.createUser(userRequest);

        assertNotNull(createdUser);
        assertEquals(userRequest.name(), createdUser.getName());
        assertEquals(userRequest.email(), createdUser.getEmail());
        assertEquals(UserRoles.CLIENTE, createdUser.getRoles());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o e-mail já existir na criação do usuário")
    void deveLancarExcecaoQuandoEmailJaExistir() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userRequest);
        });

        assertEquals("Email já está em uso.", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve validar login com credenciais corretas")
    void deveValidarLoginComCredenciaisCorretas() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertTrue(userService.validateLogin("test@example.com", "password123"));
    }

    @Test
    @DisplayName("Deve lançar InvalidCredentialsException quando o e-mail não for encontrado no login")
    void deveLancarExcecaoQuandoEmailNaoEncontradoNoLogin() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.validateLogin("nonexistent@example.com", "password123");
        });

        assertEquals("Email não encontrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar InvalidCredentialsException quando a senha estiver incorreta no login")
    void deveLancarExcecaoQuandoSenhaIncorretaNoLogin() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.validateLogin("test@example.com", "wrongpassword");
        });

        assertEquals("Senha inválida para o email informado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar a senha do usuário com sucesso")
    void deveAtualizarSenhaComSucesso() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userService.updatePassword(1L, "password123", "newPassword456");

        verify(userRepository, times(1)).save(userEntity);
        assertEquals("newEncodedPassword", userEntity.getPassword());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando a nova senha estiver em branco")
    void deveLancarExcecaoQuandoNovaSenhaEmBranco() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword(1L, "password123", "");
        });

        assertEquals("Senha não pode ser vazia.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar InvalidCredentialsException quando a senha antiga estiver incorreta na atualização")
    void deveLancarExcecaoQuandoSenhaAntigaIncorreta() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.updatePassword(1L, "wrongOldPassword", "newPassword456");
        });

        assertEquals("Senha atual incorreta.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o usuário não for encontrado na atualização de senha")
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoParaAtualizacaoSenha() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updatePassword(99L, "password123", "newPassword456");
        });

        assertEquals("Usuário não encontrado pelo id: 99", exception.getMessage());
    }

    @Test
    @DisplayName("Deve excluir um usuário com sucesso")
    void deveExcluirUsuarioComSucesso() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        doNothing().when(userRepository).delete(userEntity);

        userService.delete(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException ao tentar excluir um usuário inexistente")
    void deveLancarExcecaoAoExcluirUsuarioInexistente() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.delete(99L);
        });

        assertEquals("Usuário não encontrado pelo id: 99", exception.getMessage());
        verify(userRepository, never()).delete(any(UserEntity.class));
    }
}
