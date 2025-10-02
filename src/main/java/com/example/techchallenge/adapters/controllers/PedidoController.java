package com.example.techchallenge.adapters.controllers;

import com.example.techchallenge.core.domain.entities.Pedido;
import com.example.techchallenge.core.services.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.criarPedido(pedido);
        return ResponseEntity.ok(novoPedido);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Pedido> finalizarPedido(@PathVariable Long id) {
        Pedido pedidoFinalizado = pedidoService.finalizarPedido(id);
        return ResponseEntity.ok(pedidoFinalizado);
    }
}
