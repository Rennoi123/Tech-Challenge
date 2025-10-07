package com.example.techchallenge.adapters.controllers.graphql;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;
import com.example.techchallenge.core.domain.usecase.item.CreateItemUseCase;
import com.example.techchallenge.core.domain.usecase.item.DeleteItemUseCase;
import com.example.techchallenge.core.domain.usecase.item.GetItemByIdUseCase;
import com.example.techchallenge.core.domain.usecase.item.ListItemsUseCase;
import com.example.techchallenge.core.domain.usecase.item.UpdateItemUseCase;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class ItemGraphqlController {

    private final ListItemsUseCase listItems;
    private final GetItemByIdUseCase getItemById;
    private final CreateItemUseCase createItem;
    private final UpdateItemUseCase updateItem;
    private final DeleteItemUseCase deleteItem;

    public ItemGraphqlController(ListItemsUseCase listItems,
                                 GetItemByIdUseCase getItemById,
                                 CreateItemUseCase createItem,
                                 UpdateItemUseCase updateItem,
                                 DeleteItemUseCase deleteItem) {
        this.listItems = listItems;
        this.getItemById = getItemById;
        this.createItem = createItem;
        this.updateItem = updateItem;
        this.deleteItem = deleteItem;
    }

    @QueryMapping
    public List<Item> items() { return listItems.execute(); }

    @QueryMapping
    public Optional<Item> itemById(@Argument Long id) { return getItemById.execute(id); }

    @MutationMapping
    public ItemResponse createItem(@Argument ItemInput input) {
        ItemDTO dto = new ItemDTO(
                input.name(),
                input.description(),
                input.price(),
                input.dineInOnly(),
                input.photoPath(),
                input.restaurantId()
        );
        return createItem.execute(dto);
    }

    @MutationMapping
    public ItemResponse updateItem(@Argument Long id, @Argument ItemInput input) {
        ItemDTO dto = new ItemDTO(
                input.name(),
                input.description(),
                input.price(),
                input.dineInOnly(),
                input.photoPath(),
                input.restaurantId()
        );
        return updateItem.execute(id, dto);
    }

    @MutationMapping
    public Boolean deleteItem(@Argument Long id) {
        deleteItem.execute(id);
        return true;
    }

    public record ItemInput(
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String photoPath,
            Long restaurantId
    ) {}

    @GraphQlExceptionHandler
    public GraphQLError handle(Exception ex) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage()).build();
    }
}
