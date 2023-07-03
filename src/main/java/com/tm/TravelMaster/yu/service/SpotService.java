package com.tm.TravelMaster.yu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.TravelMaster.yu.model.Spot;
import com.tm.TravelMaster.yu.model.SpotRepository;

import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

@Service	
public class SpotService {
	
	@Autowired
	private SpotRepository sRepo;
	
	public Spot insert(Spot spot) {
		return sRepo.save(spot);
	}
	
	public List<String> getAllCityRegions() {
	    return sRepo.findAllCityRegions();
	}

	public List<String> getAllCityNames() {
	    return sRepo.findAllCityNames();
	}

	public List<String> getAllSpotTypes() {
	    return sRepo.findAllSpotTypes();
	}
	
	public Spot findById(Integer spotNo) {
		Optional<Spot> optional = sRepo.findById(spotNo);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Spot> findAll(){
		return sRepo.findAll();
	}
	
	@Transactional
	public Spot update(Spot spot) {
	    Optional<Spot> optional = sRepo.findById(spot.getSpotNo());
	    
	    if (optional.isPresent()) {
	        Spot newSpot = optional.get();
	        newSpot.setSpotName(spot.getSpotName());
	        newSpot.setCityRegion(spot.getCityRegion());
	        newSpot.setCityName(spot.getCityName());
	        newSpot.setSpotType(spot.getSpotType());
	        newSpot.setSpotInfo(spot.getSpotInfo());
	        return newSpot;
	    }
	    return null;
	}
	

	public void deleteById(Integer spotNo) {
		sRepo.deleteById(spotNo);
	}

	public List<Spot> searchSpots(String cityRegion, String cityName, String spotType) {
	    Specification<Spot> spec = Specification.where(null);

	    if (cityRegion != null && !cityRegion.isEmpty()) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("cityRegion"), cityRegion));
	    }
	    if (cityName != null && !cityName.isEmpty()) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("cityName"), cityName));
	    }
	    if (spotType != null && !spotType.isEmpty()) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("spotType"), spotType));
	    }
	    return sRepo.findAll(spec);
	}

	




}
