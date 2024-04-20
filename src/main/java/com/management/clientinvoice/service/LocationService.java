package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.dto.LocationDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface LocationService {
    List<LocationDTO> getAllLocation();
}
