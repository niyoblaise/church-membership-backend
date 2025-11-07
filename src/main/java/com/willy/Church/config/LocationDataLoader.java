package com.willy.Church.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.willy.Church.model.Location;
import com.willy.Church.model.enums.LocationType;
import com.willy.Church.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

@Component
@Order(1)
public class LocationDataLoader implements CommandLineRunner {

    private final LocationRepository locationRepository;

    public LocationDataLoader(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Map<String, String> PROVINCE_CODES = Map.of(
            "Umujyi wa Kigali", "KGL",
            "Iburasirazuba",    "EST",
            "Iburengerazuba",   "WST",
            "Amajyaruguru",     "NTH",
            "Amajyepfo",        "STH"
    );

    @Override
    public void run(String... args) throws Exception {
        if (locationRepository.count() != 0) return;

        InputStream in = new ClassPathResource("rwanda_locations.json").getInputStream();
        JsonNode root = mapper.readTree(in);

        for (JsonNode provinceNode : root.get("provinces")) {
            String provName = provinceNode.get("name").asText();
            Location province = save(null, provName, LocationType.PROVINCE,
                    PROVINCE_CODES.getOrDefault(provName, provName.substring(0,3).toUpperCase()));

            for (JsonNode districtNode : provinceNode.get("districts")) {
                Location district = save(province, districtNode.get("name").asText(), LocationType.DISTRICT, null);

                for (JsonNode sectorNode : districtNode.get("sectors")) {
                    Location sector = save(district, sectorNode.get("name").asText(), LocationType.SECTOR, null);

                    for (JsonNode cellNode : sectorNode.get("cells")) {
                        Location cell = save(sector, cellNode.get("name").asText(), LocationType.CELL, null);

                        for (JsonNode villageNode : cellNode.get("villages")) {
                            save(cell, villageNode.get("name").asText(), LocationType.VILLAGE, null);
                        }
                    }
                }
            }
        }
    }

    private Location save(Location parent, String name, LocationType type, String code) {
        Location loc = new Location(name, type);
        loc.setParent(parent);
        loc.setCode(code);
        return locationRepository.save(loc);
    }
}