package com.example.techchallenge.adapters.controllers;

import com.example.techchallenge.adapters.presenters.RestaurantPresenter;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.dto.RestaurantDTO;
import com.example.techchallenge.core.dto.RestaurantResponse;
import com.example.techchallenge.core.domain.usecase.restaurant.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    private final ListRestaurantsUseCase listRestaurantsUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase,
                                GetRestaurantByIdUseCase getRestaurantByIdUseCase,
                                ListRestaurantsUseCase listRestaurantsUseCase,
                                UpdateRestaurantUseCase updateRestaurantUseCase,
                                DeleteRestaurantUseCase deleteRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.getRestaurantByIdUseCase = getRestaurantByIdUseCase;
        this.listRestaurantsUseCase = listRestaurantsUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody @Valid RestaurantDTO dto) {
        Restaurant response = createRestaurantUseCase.execute(RestaurantPresenter.fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestaurantPresenter.toResponse(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        Restaurant response = getRestaurantByIdUseCase.execute(id);
        return ResponseEntity.ok(RestaurantPresenter.toResponse(response));
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(listRestaurantsUseCase.execute().stream()
                .map(RestaurantPresenter::toResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable Long id, @RequestBody @Valid RestaurantDTO dto) {
        Restaurant toUpdate = RestaurantPresenter.fromDTO(dto);
        toUpdate.setId(id);
        Restaurant updated = updateRestaurantUseCase.execute(toUpdate);
        return ResponseEntity.ok(RestaurantPresenter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
