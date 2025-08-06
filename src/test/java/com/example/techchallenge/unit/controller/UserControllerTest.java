package com.example.techchallenge.unit.controller;

import com.example.techchallenge.controller.UserController;
import com.example.techchallenge.dto.Request.AddressRequest;
import com.example.techchallenge.dto.Request.UpdatePasswordRequest;
import com.example.techchallenge.dto.Request.UserRequest;
import com.example.techchallenge.dto.Response.AddressResponse;
import com.example.techchallenge.dto.Response.UserResponse;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.exception.InvalidCredentialsException;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        AddressRequest addressRequest = new AddressRequest(
            null, "Rua do Cliente", "789", "Casa", "Vila", "Cidade Ficticia", "SP", "98765-432"
        );
        AddressResponse addressResponse = new AddressResponse(
            1L, "Rua do Cliente", "789", "Casa", "Vila", "Cidade Ficticia", "SP", "98765-432"
        );

        userRequest = new UserRequest(
            null, "Cliente Teste", "cliente@example.com", "senha123", addressRequest, UserRoles.CLIENTE
        );

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Cliente Teste");
        userEntity.setEmail("cliente@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setRoles(UserRoles.CLIENTE);

        userResponse = new UserResponse(
            1L, "Cliente Teste", "cliente@example.com", addressResponse, UserRoles.CLIENTE
        );
    }

    @Test
    @DisplayName("Deve cadastrar um usuário cliente com sucesso")
    void deveCadastrarUsuarioClienteComSucesso() {
        when(userService.createUser(any(UserRequest.class))).thenReturn(userEntity);

        ResponseEntity<String> responseEntity = userController.createUser(userRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Usuário criado com sucesso!", responseEntity.getBody());
        verify(userService, times(1)).createUser(userRequest);
    }

    @Test
    @DisplayName("Deve cadastrar um usuário administrador/restaurante com sucesso")
    void deveCadastrarUsuarioAdminComSucesso() {
        when(userService.createUser(any(UserRequest.class), anyString())).thenReturn(userEntity);

        ResponseEntity<String> responseEntity = userController.createUserAdmin(userRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Usuário criado com sucesso!", responseEntity.getBody());
        verify(userService, times(1)).createUser(userRequest, UserRoles.RESTAURANTE.toString());
    }

    @Test
    @DisplayName("Deve fazer login do usuário com sucesso")
    void deveFazerLoginComSucesso() {
        when(userService.validateLogin(anyString(), anyString())).thenReturn(true);

        ResponseEntity<String> responseEntity = userController.loginUser(userRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Usuário logado com sucesso.", responseEntity.getBody());
        verify(userService, times(1)).validateLogin(userRequest.email(), userRequest.password());
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    void deveRetornarTodosUsuarios() {
        List<UserEntity> userEntities = Collections.singletonList(userEntity);
        List<UserResponse> expectedResponses = Collections.singletonList(userResponse);

        when(userService.getAll()).thenReturn(userEntities);
        when(userService.toResponse(any(UserEntity.class))).thenReturn(userResponse);

        ResponseEntity<List<UserResponse>> responseEntity = userController.getAllUsers();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponses, responseEntity.getBody());
        verify(userService, times(1)).getAll();
        verify(userService, times(1)).toResponse(userEntity);
    }

    @Test
    @DisplayName("Deve retornar um usuário pelo ID")
    void deveRetornarUsuarioPorId() {
        when(userService.getById(anyLong())).thenReturn(userEntity);
        when(userService.toResponse(any(UserEntity.class))).thenReturn(userResponse);

        ResponseEntity<UserResponse> responseEntity = userController.getUserById(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
        verify(userService, times(1)).getById(1L);
        verify(userService, times(1)).toResponse(userEntity);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND quando o usuário pelo ID não existir")
    void deveRetornarNotFoundQuandoUsuarioNaoExistePorId() {
        when(userService.getById(anyLong())).thenThrow(new UserNotFoundException("Usuário não encontrado"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.getUserById(99L);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userService, times(1)).getById(99L);
        verify(userService, never()).toResponse(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar o usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        UserRequest updateRequest = new UserRequest(
            1L, "Updated Name", "updated@example.com", "newpassword", userRequest.address(), null
        );
        UserResponse updatedResponse = new UserResponse(
            1L, "Updated Name", "updated@example.com", userResponse.address(), UserRoles.CLIENTE
        );

        when(userService.updateUser(any(UserRequest.class))).thenReturn(updatedResponse);

        ResponseEntity<UserResponse> responseEntity = userController.updateUser(1L, updateRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedResponse, responseEntity.getBody());
        verify(userService, times(1)).updateUser(any(UserRequest.class));
    }

    @Test
    @DisplayName("Deve excluir o usuário com sucesso")
    void deveExcluirUsuarioComSucesso() {
        doNothing().when(userService).delete(anyLong());

        ResponseEntity<Void> responseEntity = userController.deleteUser(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao tentar excluir um usuário inexistente")
    void deveRetornarNotFoundAoExcluirUsuarioInexistente() {
        doThrow(new UserNotFoundException("Usuário não encontrado")).when(userService).delete(anyLong());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.deleteUser(99L);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userService, times(1)).delete(99L);
    }

    @Test
    @DisplayName("Deve atualizar a senha com sucesso")
    void deveAtualizarSenhaComSucesso() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("oldPass", "newPass");
        doNothing().when(userService).updatePassword(anyLong(), anyString(), anyString());

        ResponseEntity<String> responseEntity = userController.updatePassword(1L, updatePasswordRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Senha atualizada com sucesso.", responseEntity.getBody());
        verify(userService, times(1)).updatePassword(1L, "oldPass", "newPass");
    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST ao falhar ao atualizar a senha por senha antiga inv�lida")
    void deveRetornarBadRequestAoFalharAtualizacaoSenhaIncorreta() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("wrongOldPass", "newPass");
        doThrow(new InvalidCredentialsException("Senha atual incorreta.")).when(userService).updatePassword(anyLong(), anyString(), anyString());

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userController.updatePassword(1L, updatePasswordRequest);
        });

        assertEquals("Senha atual incorreta.", exception.getMessage());
        verify(userService, times(1)).updatePassword(1L, "wrongOldPass", "newPass");
    }
}
