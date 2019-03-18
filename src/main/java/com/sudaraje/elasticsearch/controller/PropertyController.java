package com.sudaraje.elasticsearch.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.sudaraje.elasticsearch.builders.SearchQueryBuilder;
import com.sudaraje.elasticsearch.model.Property;
import com.sudaraje.elasticsearch.model.PropertyQueryParams;
import com.sudaraje.elasticsearch.repository.PropertyRepository;


@RestController
public class PropertyController{
	
	@Autowired
	private PropertyRepository propertyRepository;
	
	@Autowired
    private SearchQueryBuilder searchQueryBuilder;
	
	@PostMapping("/saveProperty")
	public int saveProperty(@RequestBody List<Property> properties) {
		propertyRepository.saveAll(properties);
		return properties.size();
	}
	
	@GetMapping("/findAll")
	public Iterable<Property> findAllProperties(){
		return propertyRepository.findAll();
	}
	
	@RequestMapping("/findById/{id}")
	public Optional<Property> findById(@PathVariable("id") String id){
		return propertyRepository.findById(id);
	}
	
	@PostMapping("/filterByDistance")
	public ResponseEntity<Object> filterProperties(@RequestBody PropertyQueryParams queryParams ){		
		
		if (queryParams.getMinBudget() == 0 && queryParams.getMaxBudget() == 0)
			return new ResponseEntity<>("Either Min or Max for Budget must be present", HttpStatus.NOT_ACCEPTABLE);
		else if (queryParams.getMinBedroom() == 0 && queryParams.getMaxBedroom() == 0)
			return new ResponseEntity<>("Either Min or Max for Bedroom must be present", HttpStatus.NOT_ACCEPTABLE);
		else if (queryParams.getMinBathroom() == 0 && queryParams.getMaxBathroom() == 0)
			return new ResponseEntity<>("Either Min or Max for Bathroom must be present", HttpStatus.NOT_ACCEPTABLE);
		else if (queryParams.getLat() == 0 && queryParams.getLng() == 0)
			return new ResponseEntity<>("Lat and Long must be present", HttpStatus.NOT_ACCEPTABLE);
		
		return new ResponseEntity<>(searchQueryBuilder.customSearch(queryParams),HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping(value = "/search/{text}")
    public List<Property> getAll(@PathVariable final String text) {
        return searchQueryBuilder.getAll(text);
    }
		
	
}