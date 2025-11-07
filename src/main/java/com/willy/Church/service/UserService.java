package com.willy.Church.service;

import com.willy.Church.dto.LocationDTO;
import com.willy.Church.dto.UserDTO;
import com.willy.Church.dto.UserUpdateDto;
import com.willy.Church.model.Location;
import com.willy.Church.model.User;
import com.willy.Church.model.enums.Role;
import com.willy.Church.repository.LocationRepository;
import com.willy.Church.repository.UserRepository;
import com.willy.Church.util.PasswordUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public UserService(UserRepository userRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }


    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> searchByName(String name, Pageable pageable) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findByProvince(String province, Pageable pageable) {
        return userRepository.findByProvinceNameOrCode(province, pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findByVillage(Long villageId) {
        return userRepository.findByVillageId(villageId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findByCell(Long cellId) {
        return userRepository.findByVillageParentId(cellId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findBySector(Long sectorId) {
        return userRepository.findByVillageParentParentId(sectorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findByDistrict(Long districtId) {
        return userRepository.findByVillageParentParentParentId(districtId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findByDateOfBirthBetween(LocalDate from, LocalDate to) {
        return userRepository.findByDateOfBirthBetween(from, to).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findBirthdayToday() {
        LocalDate today = LocalDate.now();
        return userRepository.findBirthdayByMonthAndDay(today.getMonthValue(), today.getDayOfMonth()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countByRole(Role role) {
        return userRepository.countByRole(role);
    }

    @Transactional
    public User createMember(String firstName, String lastName, String email, String rawPassword,
                             LocalDate dateOfBirth, String phoneNumber, Role role, Long villageId) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already registered");
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalStateException("Phone number already registered");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(PasswordUtil.hash(rawPassword));
        user.setDateOfBirth(dateOfBirth);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        user.setVillage(locationRepository.getReferenceById(villageId));
        return userRepository.save(user);
    }

    @Transactional
    public User updateMember(Long userId, UserUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (dto.getEmail() != null &&
                !user.getEmail().equalsIgnoreCase(dto.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already in use by another account");
        }
        if (dto.getPhoneNumber() != null &&
                !user.getPhoneNumber().equalsIgnoreCase(dto.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalStateException("Phone number already in use by another account");
        }


        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getVillageId() != null) {
            if (!locationRepository.existsById(dto.getVillageId()))
                throw new IllegalArgumentException("Village id=" + dto.getVillageId() + " not found");
            user.setVillage(locationRepository.getReferenceById(dto.getVillageId()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String rawNewPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(PasswordUtil.hash(rawNewPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<String> findProvinceNameByUserId(Long userId) {
        return userRepository.findProvinceNameByUserId(userId);
    }

    /* ---------- helper ---------- */

    public UserDTO convertToDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setVillage(convertLocationToDTO(user.getVillage()));
        return dto;
    }

    private LocationDTO convertLocationToDTO(Location location) {
        if (location == null) return null;
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setType(location.getType());
        dto.setCode(location.getCode());
        dto.setChildren(location.getChildren().stream()
                .map(this::convertLocationToDTO)
                .collect(Collectors.toList()));
        return dto;
    }
}