package com.willy.Church.repository;


import com.willy.Church.model.User;
import com.willy.Church.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);

    List<User> findByVillageId(Long villageId);
    List<User> findByVillageParentId(Long cellId);
    List<User> findByVillageParentParentId(Long sectorId);
    List<User> findByVillageParentParentParentId(Long districtId);

    @Query("SELECT u FROM User u " +
            "JOIN u.village v " +
            "JOIN v.parent c " +
            "JOIN c.parent s " +
            "JOIN s.parent d " +
            "JOIN d.parent p " +
            "WHERE p.name = :province OR p.code = :province")
    Page<User> findByProvinceNameOrCode(@Param("province") String province, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.dateOfBirth BETWEEN :from AND :to")
    List<User> findByDateOfBirthBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT u FROM User u WHERE FUNCTION('MONTH', u.dateOfBirth) = :month AND FUNCTION('DAY', u.dateOfBirth) = :day")
    List<User> findBirthdayByMonthAndDay(@Param("month") int month, @Param("day") int day);

    long countByRole(Role role);

    @Query("""
    SELECT p.name 
    FROM User u
    JOIN u.village v
    JOIN v.parent c
    JOIN c.parent s
    JOIN s.parent d
    JOIN d.parent p
    WHERE u.id = :userId AND p.type = 'PROVINCE'
""")
    Optional<String> findProvinceNameByUserId(@Param("userId") Long userId);
}