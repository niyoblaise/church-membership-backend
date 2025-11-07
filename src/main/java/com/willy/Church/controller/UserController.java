package com.willy.Church.controller;

import com.willy.Church.dto.PasswordChangeDto;
import com.willy.Church.dto.UserCreateDto;
import com.willy.Church.dto.UserDTO;
import com.willy.Church.dto.UserUpdateDto;
import com.willy.Church.model.User;
import com.willy.Church.model.enums.Role;
import com.willy.Church.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Page<UserDTO>> all(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> one(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "province")
    public ResponseEntity<Page<UserDTO>> byProvince(@RequestParam String province,
                                                    @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.findByProvince(province, pageable));
    }

    @GetMapping("/{userId}/province")
    public ResponseEntity<String> getUserProvince(@PathVariable Long userId) {
        Optional<String> provinceName = userService.findProvinceNameByUserId(userId);
        return provinceName.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = "role")
    public ResponseEntity<Page<UserDTO>> byRole(@RequestParam Role role, Pageable pageable) {
        return ResponseEntity.ok(userService.findByRole(role, pageable));
    }

    @GetMapping(params = "name")
    public ResponseEntity<Page<UserDTO>> searchName(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(userService.searchByName(name, pageable));
    }

    @GetMapping(params = "villageId")
    public ResponseEntity<List<UserDTO>> byVillage(@RequestParam Long villageId) {
        return ResponseEntity.ok(userService.findByVillage(villageId));
    }

    @GetMapping(params = "cellId")
    public ResponseEntity<List<UserDTO>> byCell(@RequestParam Long cellId) {
        return ResponseEntity.ok(userService.findByCell(cellId));
    }

    @GetMapping(params = "sectorId")
    public ResponseEntity<List<UserDTO>> bySector(@RequestParam Long sectorId) {
        return ResponseEntity.ok(userService.findBySector(sectorId));
    }

    @GetMapping(params = "districtId")
    public ResponseEntity<List<UserDTO>> byDistrict(@RequestParam Long districtId) {
        return ResponseEntity.ok(userService.findByDistrict(districtId));
    }

    @GetMapping(params = "email")
    public ResponseEntity<UserDTO> byEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "phoneNumber")
    public ResponseEntity<UserDTO> byPhoneNumber(@RequestParam String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/birthday-today")
    public ResponseEntity<List<UserDTO>> birthdayToday() {
        return ResponseEntity.ok(userService.findBirthdayToday());
    }

    @GetMapping(params = {"dobFrom", "dobTo"})
    public ResponseEntity<List<UserDTO>> dobRange(@RequestParam LocalDate dobFrom,
                                                  @RequestParam LocalDate dobTo) {
        return ResponseEntity.ok(userService.findByDateOfBirthBetween(dobFrom, dobTo));
    }

    @GetMapping("/count-by-role")
    public ResponseEntity<Long> countByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.countByRole(role));
    }


    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserCreateDto dto) {
        User entity = userService.createMember(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getRawPassword(),
                dto.getDateOfBirth(),
                dto.getPhoneNumber(),
                dto.getRole(),
                dto.getVillageId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToDTO(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,
                                          @RequestBody UserUpdateDto dto) {
        User entity = userService.updateMember(id, dto);
        return ResponseEntity.ok(userService.convertToDTO(entity));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @RequestBody PasswordChangeDto dto) {
        userService.changePassword(id, dto.getRawNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}