package com.vehiclerental.repository;

import com.vehiclerental.entity.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Long> {
    Optional<VehicleCategory> findByName(String name);
    Boolean existsByName(String name);
}
