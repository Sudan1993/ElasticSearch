package com.sudaraje.elasticsearch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sudaraje.elasticsearch.model.Property;

@Repository
public interface PropertyRepository extends ElasticsearchRepository<Property,String>{
	
	@Query("SELECT p from Property p, "
			+ "111.111 * "
			+ "DEGREES(ACOS(LEAST(COS(RADIANS(a.Latitude)) "
			+ "* COS(RADIANS(:lat))  "
			+ "* COS(RADIANS(p.lng - :lng)) "
			+ "+ SIN(RADIANS(p.lat))"
			+ "* SIN(RADIANS(:lat)), 1.0))) "
			+ "AS distance_in_km HAVING (distance_in_km < 10)")
	List<Property> sortByDistance(@Param("lat")double lat, 
								  @Param("lng")double lng);
	
	@Query("{\"query\": {\"range\" : {\"budget\" : {\"gte\" : \"?0\",\"lte\" : \"?1\"}}}}")
	Page<Property> getWithinRange(@Param("minBudget")int minBudget, @Param("maxBudget")int maxBudget,Pageable pageable);
			
}
