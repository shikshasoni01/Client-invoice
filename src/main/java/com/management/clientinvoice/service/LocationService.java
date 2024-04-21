package com.management.clientinvoice.service;

import com.management.clientinvoice.dto.LocationDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface LocationService {
    List<LocationDTO> getAllLocation();
}
