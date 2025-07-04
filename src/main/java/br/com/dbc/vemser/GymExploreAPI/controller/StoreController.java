package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.StoreItemCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.StoreItem;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Validated
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/items")
    public ResponseEntity<List<StoreItem>> listItems() {
        return ResponseEntity.ok(storeService.listAllItems());
    }

    @PostMapping("/purchase/user/{userId}/item/{itemId}")
    public ResponseEntity<?> purchaseItem(@PathVariable Long userId, @PathVariable Integer itemId) {
        try {
            storeService.purchaseItem(userId, itemId);
            return ResponseEntity.ok(Map.of("message", "Troca realizada com sucesso!"));
        } catch (RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/items")
    public ResponseEntity<StoreItem> createStoreItem(@Valid @RequestBody StoreItemCreateDTO itemCreateDTO) {
        StoreItem newItem = storeService.createItem(itemCreateDTO);
        return ResponseEntity.ok(newItem);
    }

    @PutMapping("/admin/items/{itemId}")
    public ResponseEntity<StoreItem> updateStoreItem(
            @PathVariable Integer itemId,
            @Valid @RequestBody StoreItemCreateDTO itemUpdateDTO) throws RegraDeNegocioException {
        StoreItem updatedItem = storeService.updateItem(itemId, itemUpdateDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/admin/items/{itemId}")
    public ResponseEntity<Void> deleteStoreItem(@PathVariable Integer itemId) throws RegraDeNegocioException {
        storeService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}