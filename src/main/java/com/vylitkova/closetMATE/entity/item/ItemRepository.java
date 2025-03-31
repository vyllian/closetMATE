package com.vylitkova.closetMATE.entity.item;

import com.vylitkova.closetMATE.entity.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Transactional
    @Query(value = "select colors from item_colors where item_id=?1", nativeQuery = true)
    public List<Object[]> getItemColors(UUID id);

}
