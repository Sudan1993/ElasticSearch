package com.sudaraje.elasticsearch.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	public Iterable<Property> filterProperties(@RequestBody PropertyQueryParams queryParams ){
		//get the post params 
		double lat = queryParams.getLat();
		double lng = queryParams.getLng();
		int minBudget = queryParams.getMinBudget();
		int maxBudget = queryParams.getMaxBudget();
		int minBedroom = queryParams.getMinBathroom();
		int maxBedroom = queryParams.getMaxBathroom();
		int minBathroom = queryParams.getMinBathroom();
		int maxBathroom = queryParams.getMaxBathroom();
		
		String query_param = "select * from Property p where p.id > 350";
		//String query_param = "{\"query\": { \"query_string\" : { \"default_field\" : \"content\", \"query\" : \"77.5009\" }}}";
		//String query_param = "{\"range\" : {\"budget\" : {\"gte\" : \"" + queryParams.getMinBudget() + "\",\"lte\" : \""+ queryParams.getMaxBudget() +"\"}}}";
		//Iterable<Property> propList = propertyRepository.search(queryStringQuery(query_param));
		//List<Property> propList = propertyRepository.customQuery(26000, 30000);
		//return propList;
		//return propertyRepository.findByBudget(20684);
		return propertyRepository.findByBudgetBetween(minBudget,maxBudget);
		
	}
	
	@GetMapping(value = "/search/{text}")
    public List<Property> getAll(@PathVariable final String text) {
        return searchQueryBuilder.getAll(text);
    }
		
}