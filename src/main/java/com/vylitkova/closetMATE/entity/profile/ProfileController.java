package com.vylitkova.closetMATE.entity.profile;

import com.vylitkova.closetMATE.api.AWSBucketService;
import com.vylitkova.closetMATE.dto.ItemDetails;
import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.outfit.Outfit;
import com.vylitkova.closetMATE.entity.utility.ImageInfo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path="api/profile")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AWSBucketService awsBucketService;

    @PostMapping("/add")
    public UUID addProfile(@RequestBody @Valid Profile profile) {
        System.out.println(profile.toString());
        return profileService.saveProfile(profile) ;
    }
    @PostMapping("/uploadPhoto")
    public ImageInfo uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            return profileService.uploadImage(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable("id") UUID id) {
        return profileService.getProfile(id).orElseThrow(EntityNotFoundException::new);
    }
    @GetMapping("/{id}/photoUrl")
    public String getNewUrl(@PathVariable("id") UUID id) {
        return profileService.getProfileImgUrl(id);
    }

    @GetMapping("/detailed/{id}")
    public Map<String, String> getProfileDetailed(@PathVariable("id") UUID id) {
        return profileService.getFullProfile(id);
    }

    @GetMapping("/{id}/items")
    public List<Item> getProfileItems(@PathVariable("id") UUID id) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        return profileService.getItems(id);
    }
    @GetMapping("/{id}/items-by-category")
    public List<ItemDetails> getProfileItems(@PathVariable("id") UUID id,  @RequestParam("category") String category) {
        return profileService.getItemsByCategory(id, category);
    }

    @GetMapping("/{id}/fav-items")
    public List<Item> getProfileFavItems(@PathVariable("id") UUID id) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        return profileService.getFavItems(id);
    }

    @PutMapping("/{id}/add-item")
    public void addItem(@PathVariable UUID id, @RequestBody @Valid Item new_item) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        profileService.addItem(id, new_item);
    }

    @PutMapping("/{id}/set-item-fav")
    public void setItemFav(@PathVariable UUID id, @RequestParam("item-id") UUID item_id) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        profileService.setItemFavourite(id, item_id);
    }

    @GetMapping("/{id}/outfits")
    public List<Outfit> getProfileOutfits(@PathVariable("id") UUID id) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        return profileService.getOutfits(id);
    }

    @GetMapping("/{id}/fav_outfits")
    public List<Outfit> getProfileFavOutfits(@PathVariable("id") UUID id) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        return profileService.getFavOutfits(id);
    }

    @PutMapping("/{id}/addOutfit")
    public void addOutfit(@PathVariable UUID id, @RequestBody @Valid UUID new_outfit) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        profileService.addOutfit(id, new_outfit);
    }

    @PutMapping("/{id}/setOutfitFav/{outfit_id}")
    public void setOutfitFav(@PathVariable UUID id, @PathVariable UUID outfit_id) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        profileService.setOutfitFavourite(id, outfit_id);
    }

    @PutMapping("/edit/{id}")
    public void update(@PathVariable UUID id, @Valid @RequestBody Profile new_profile) {
        if(profileService.getProfile(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        profileService.updateProfile(id, new_profile);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        profileService.deleteProfile(id);
    }


}
