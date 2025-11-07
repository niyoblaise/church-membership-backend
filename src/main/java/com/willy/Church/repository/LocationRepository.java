package com.willy.Church.repository;

import com.willy.Church.model.Location;
import com.willy.Church.model.enums.LocationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByNameAndParent(String name, Location parent);
    boolean existsByNameAndType(String name, LocationType type);
    Optional<Location> findByNameAndType(String name, LocationType type);
    List<Location> findByType(LocationType type);
    List<Location> findByParent(Location parent);

    Page<Location> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    @Query("SELECT l FROM Location l LEFT JOIN FETCH l.children WHERE l.parent IS NULL")
    List<Location> findProvincesWithChildren();
    long countByParent(Location parent);
    Optional<Location> findByCode(String code);

}