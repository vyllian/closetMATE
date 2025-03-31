package com.vylitkova.closetMATE.entity.item;

import com.vylitkova.closetMATE.entity.profile.Profile;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/item")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping("/new")
    public Item categoriesItem(@RequestParam("file") MultipartFile file) {
        try {
            return itemService.categoriseItem(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/add")
    public void addItem(@RequestBody @Valid Item newItem) {
       itemService.saveItem(newItem);
    }
    @GetMapping("/{id}")
    public Item getItem(@PathVariable UUID id) {
        return itemService.getItem(id).orElseThrow(EntityNotFoundException::new);
    }
    @PutMapping("/edit/{id}")
    public void updateItem(@PathVariable UUID id, @RequestBody @Valid Item newItem) {
        itemService.updateItem(id, newItem);
    }
    @DeleteMapping("/delete-image")
    public void deleteImg(@RequestBody String image) {
        itemService.deleteImage(image);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        itemService.deleteItem(id);
    }
}
