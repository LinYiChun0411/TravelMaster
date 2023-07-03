package com.tm.TravelMaster.yu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tm.TravelMaster.yu.model.Spot;
import com.tm.TravelMaster.yu.service.SpotService;

@Controller
public class SpotFrontController {

	@Autowired
	private SpotService spotService;

	@GetMapping("/toSpot")
	public String toSpot() {
		return "yu/Index";
	}

	@GetMapping("/allSpot")
	public String allSpot(Model model) {
	    List<Spot> spots = spotService.findAll();
	    List<String> cityRegions = spotService.getAllCityRegions();
	    List<String> cityNames = spotService.getAllCityNames();
	    List<String> spotTypes = spotService.getAllSpotTypes();
	    
	    model.addAttribute("spots", spots);
	    model.addAttribute("cityRegions", cityRegions);
	    model.addAttribute("cityNames", cityNames);
	    model.addAttribute("spotTypes", spotTypes);
	    
	    return "yu/AllSpot";
	}

	
	@GetMapping("/spotList/searchSpot")
	public String searchSpot(@RequestParam(value = "cityRegion", required = false) String cityRegion,
	                         @RequestParam(value = "cityName", required = false) String cityName,
	                         @RequestParam(value = "spotType", required = false) String spotType,
	                         @RequestParam(value = "txtQuery", required = false) String txtQuery,
	                         Model model) {
	    List<Spot> spots = spotService.searchSpots(cityRegion, cityName, spotType);
	    model.addAttribute("spots", spots);
	    return "yu/AllSpot";
	}
	
	@GetMapping("/spotDetails/{spotNo}")
	public String spotDetails(@PathVariable("spotNo") Integer spotNo, Model model) {
		Spot spot = spotService.findById(spotNo);
		model.addAttribute("spot", spot);
		return "yu/SpotDetails";
	}

	@GetMapping("/likedSpot")
	public String likedSpot() {
		return "yu/LikedSpot";
	}

	@GetMapping("/admin")
	public String admin() {
		return "yu/Admin";
	}

}
