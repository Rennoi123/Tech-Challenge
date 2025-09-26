package com.example.techchallenge.adapters.controllers;

import com.example.techchallenge.adapters.presenters.ItemPresenter;
import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.usecase.item.*;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemByIdUseCase getItemByIdUseCase;
    private final ListItemsUseCase listItemsUseCase;
    private final UpdateItemUseCase updateItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;
    private final FindItemsByRestaurantUseCase findItemsByRestaurantUseCase;
    private final FindItemsByNameUseCase findItemsByNameUseCase;

    public ItemController(CreateItemUseCase createItemUseCase,
                          GetItemByIdUseCase getItemByIdUseCase,
                          ListItemsUseCase listItemsUseCase,
                          UpdateItemUseCase updateItemUseCase,
                          DeleteItemUseCase deleteItemUseCase,
                          FindItemsByRestaurantUseCase findItemsByRestaurantUseCase,
                          FindItemsByNameUseCase findItemsByNameUseCase) {
        this.createItemUseCase = createItemUseCase;
        this.getItemByIdUseCase = getItemByIdUseCase;
        this.listItemsUseCase = listItemsUseCase;
        this.updateItemUseCase = updateItemUseCase;
        this.deleteItemUseCase = deleteItemUseCase;
        this.findItemsByRestaurantUseCase = findItemsByRestaurantUseCase;
        this.findItemsByNameUseCase = findItemsByNameUseCase;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(@RequestBody @Valid ItemDTO dto) {
        ItemResponse itemCreated = createItemUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getById(@PathVariable Long id) {
        return getItemByIdUseCase.execute(id)
                .map(ItemPresenter::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(listItemsUseCase.execute().stream().map(ItemPresenter::toResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(@PathVariable Long id, @RequestBody @Valid ItemDTO dto) {
        ItemResponse itemUpdated = updateItemUseCase.execute(id, dto);
        return ResponseEntity.ok(itemUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> listByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(findItemsByRestaurantUseCase.execute(restaurantId).stream()
                .map(ItemPresenter::toResponse).toList());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(findItemsByNameUseCase.execute(name).stream()
                .map(ItemPresenter::toResponse).toList());
    }
}
