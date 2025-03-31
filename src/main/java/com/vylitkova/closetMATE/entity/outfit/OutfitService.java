package com.vylitkova.closetMATE.entity.outfit;

import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.item.ItemRepository;
import com.vylitkova.closetMATE.entity.item.enums.Color;
import com.vylitkova.closetMATE.entity.joinTables.Outfit_items;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class OutfitService {
    private final OutfitRepository outfitRepository;
    private final ItemRepository itemRepository;

    public Optional<Outfit> getOutfit(UUID id) {
        return outfitRepository.findById(id);
    }
    public List<Outfit> getOutfitsByDate(LocalDate date) {
        List<UUID> ids =outfitRepository.findAllByPlanningToWearContaining(date);
        List<Outfit> outfits = new ArrayList<>();
        for (UUID id : ids) {
            outfits.add(outfitRepository.findById(id).get());
        }
        return outfits;
    }
    public void saveOutfit(Outfit outfit, HashMap<Integer, UUID> items) {
        Outfit_items outfitItem = new Outfit_items();
        outfit.getOutfit_items();
        outfitItem.setOutfit(outfit);
        if (outfit.getOutfit_items() == null) {
            outfit.setOutfit_items(new ArrayList<>());
        }
        for ( int item : items.keySet()) {
            outfitItem.setItem(itemRepository.findById(items.get(item)).orElse(null));
            outfitItem.setPosition(item);
            outfit.getOutfit_items().add(outfitItem);
        }
        outfitRepository.save(outfit);
    }
    public Set<String> defineOutfitColors(Outfit outfit) {
        Set<String> outfitColors = new HashSet<>();
        for ( Outfit_items outfit_item : outfit.getOutfit_items()) {
            outfitColors.addAll(outfit_item.getItem().getColors());
        }
        return outfitColors;
    }
    public void updateOutfitDesc(UUID id, String desc) {
        Outfit oldOutfit = outfitRepository.findById(id).orElse(null);
        oldOutfit.setDescription(desc);
        outfitRepository.save(oldOutfit);
    }
    public void addDate(UUID id, LocalDate date) {
        Outfit outfit = outfitRepository.findById(id).orElse(null);
        outfit.getPlanningToWear().add(date);
        outfitRepository.save(outfit);
    }
    public void removeDate(UUID id, LocalDate date) {
        Outfit outfit = outfitRepository.findById(id).orElse(null);
        outfit.getPlanningToWear().remove(date);
        outfitRepository.save(outfit);
    }

    public void deleteOutfit(UUID id) {
        outfitRepository.deleteById(id);
    }

}
