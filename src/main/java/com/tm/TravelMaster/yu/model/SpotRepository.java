package com.tm.TravelMaster.yu.model;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SpotRepository extends JpaRepository<Spot, Integer> {
	
	@Query("SELECT DISTINCT s.cityRegion FROM Spot s")
	List<String> findAllCityRegions();

	@Query("SELECT DISTINCT s.cityName FROM Spot s")
	List<String> findAllCityNames();

	@Query("SELECT DISTINCT s.spotType FROM Spot s")
	List<String> findAllSpotTypes();
	
	@Query("FROM Spot WHERE cityRegion = :cityRegion")
	public Spot findBycityRegion(@Param("cityRegion") String cityRegion);
	
	@Query("FROM Spot WHERE cityName = :cityName")
	public Spot findBycityName(@Param("cityName") String cityName);
	
	@Query("FROM Spot WHERE spotType = :spotType")
	public Spot findByspotType(@Param("spotType") String spotType);

	@Query("SELECT s FROM Spot s WHERE s.cityRegion = :cityRegion " +
		       "AND s.cityName = :cityName AND s.spotType = :spotType " +
		       "AND (s.spotName LIKE %:txtQuery% OR s.spotInfo LIKE %:txtQuery%)")
		List<Spot> searchSpots(@Param("cityRegion") String cityRegion,
		                       @Param("cityName") String cityName,
		                       @Param("spotType") String spotType,
		                       @Param("txtQuery") String txtQuery);

	List<Spot> findAll(Specification<Spot> spec);

	

	
}
