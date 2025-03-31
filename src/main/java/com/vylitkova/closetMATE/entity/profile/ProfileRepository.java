package com.vylitkova.closetMATE.entity.profile;

import com.vylitkova.closetMATE.dto.ItemDetails;
import com.vylitkova.closetMATE.entity.joinTables.Profile_items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    @Modifying
    @Transactional
    @Query(value = "update profile_items set favourite=?3 where profile_id = ?1 and item_id = ?2" ,nativeQuery = true)
    public void updateFavouriteItem(UUID profile_id, UUID item_id, boolean favourite);

    @Transactional
    @Query(value = "select favourite from profile_items where item_id = ?1", nativeQuery = true)
    public boolean getFavStatus(UUID item_id);

    @Modifying
    @Transactional
    @Query(value = "update profile_outfits set favourite=true where profile_id = ?1 and outfit_id = ?2",nativeQuery = true)
    public void updateFavouriteOutfit(UUID profile_id, UUID outfit_id);

    @Transactional
    @Query(value = "SELECT p.bio, p.image, u.first_name, u.last_name FROM profiles p JOIN users u ON p.user_id = u.id where p.id=?1", nativeQuery = true)
    public Map<String, String> getProfileInfo(UUID profile_id);

    @Transactional
    @Query(value = "SELECT favourite, i.id, i.image, i.public_url, i.url_expiry_date,i.formality, i.material, i.mood, i.pattern, i.purpose, i.season, i.style, i.type, i.category, i.color_warmness , i.color_darkness, i.temperature_min, i.temperature_max from profile_items right join public.items i on profile_items.item_id = i.id where profile_id =?1 and category =?2", nativeQuery = true)
    List<Object[]> getItemsByCategory(UUID id, String category);

//    @Transactional
//    @Query(value = """
//            SELECT new com.vylitkova.closetMATE.dto.ItemDetails(p.item.id, p.favourite, p.item.image, p.item.publicUrl, p.item.urlExpiryDate,
//                p.item.formality, p.item.material, p.item.mood, p.item.pattern, p.item.purpose, p.item.season, p.item.style, p.item.type, p.item.category, p.item.color_warmness,
//                p.item.colors, p.item.color_darkness, p.item.temperature_min, p.item.temperature_max)
//                FROM Profile_items  p
//                WHERE p.profile.id = ?1 AND p.item.category = ?2""")
//    List<ItemDetails> getItemsByCat(UUID profile_id, String category);
//
}
