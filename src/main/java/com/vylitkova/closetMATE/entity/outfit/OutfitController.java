package com.vylitkova.closetMATE.entity.outfit;

import com.vylitkova.closetMATE.entity.item.Item;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/outfit")
@AllArgsConstructor
public class OutfitController {
    private final OutfitService outfitService;

    @PostMapping("/add")
    public void addOutfit(@RequestBody @Valid OutfitRequest outfitRequest) {
        outfitService.saveOutfit(outfitRequest.getOutfit(), outfitRequest.getItems());
    }

    @GetMapping("/{id}")
    public Outfit getOutfit(@PathVariable UUID id) {
        return outfitService.getOutfit(id).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/byDate/{date}")
    public List<Outfit> getOutfitsByDate(@PathVariable String date) {
        return outfitService.getOutfitsByDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @PatchMapping("/{id}/new_description")
    public void updateOutfit(@PathVariable UUID id, @RequestBody @Valid String description) {
        outfitService.updateOutfitDesc(id, description);
    }

//    @PatchMapping("/{id}/define_colors")
//    public void updateOutfitColors(@PathVariable UUID id) {
//        Outfit outfit = outfitService.getOutfit(id).orElseThrow(EntityNotFoundException::new);
//        outfitService.setOutfitColors(outfit);
//    }

    @PatchMapping("/{id}/new_date")
    public void addOutfitDate(@PathVariable UUID id, @RequestBody @Valid String date) {
        outfitService.addDate(id, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @PatchMapping("/{id}/rem_date")
    public void removeOutfitDate(@PathVariable UUID id, @RequestBody @Valid String date) {
        outfitService.removeDate(id, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
       outfitService.deleteOutfit(id);
    }


}
