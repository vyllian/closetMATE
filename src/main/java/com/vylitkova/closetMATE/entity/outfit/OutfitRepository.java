package com.vylitkova.closetMATE.entity.outfit;

import com.vylitkova.closetMATE.entity.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface OutfitRepository extends JpaRepository<Outfit, UUID> {

    @Query(value = "select outfit_id from outfit_planning_to_wear where planning_to_wear = ?1", nativeQuery = true)
    List<UUID> findAllByPlanningToWearContaining( LocalDate planningToWear);

}
