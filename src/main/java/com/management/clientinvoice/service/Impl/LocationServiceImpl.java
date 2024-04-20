package com.biz4solutions.clientinvoice.service.Impl;


import com.biz4solutions.clientinvoice.domain.Location;
import com.biz4solutions.clientinvoice.dto.LocationDTO;
import com.biz4solutions.clientinvoice.repository.LocationRepository;
import com.biz4solutions.clientinvoice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<LocationDTO> getAllLocation() {
        List<Location> locations = locationRepository.findAll();
        List<LocationDTO> locationDTOS = new ArrayList<>();
        for(Location location : locations){
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(location.getId());
            locationDTO.setArea(location.getArea());
            locationDTO.setCity(location.getCity());

            locationDTOS.add(locationDTO);
        }
        return locationDTOS;
    }
}
