package com.sudaraje.elasticsearch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;



import com.sudaraje.elasticsearch.model.Property;
import com.sudaraje.elasticsearch.model.PropertyQueryParams;
import com.sudaraje.elasticsearch.repository.PropertyRepository;


@RestController
public class PropertyController{
	
	@Autowired
	private PropertyRepository propertyRepository;
	
	@PostMapping("/saveProperty")
	public int saveProperty(@RequestBody List<Property> properties) {
		propertyRepository.saveAll(properties);
		return properties.size();
	}
	
	@GetMapping("/findAll")
	public Iterable<Property> findAllProperties(){
		return propertyRepository.findAll();
	}
	
	@PostMapping("/filterByDistance")
	public Iterable<Property> filterProperties(@RequestBody PropertyQueryParams queryParams ){
		double lat = queryParams.getLat();
		double lng = queryParams.getLng();
		String query_param = "{\"query\": {\"range\" : {\"budget\" : {\"gte\" : \"" + queryParams.getMinBudget() + "\",\"lte\" : \""+ queryParams.getMaxBudget() +"\"}}}}";
		Iterable<Property> propList = propertyRepository.search(queryStringQuery(query_param));
		return propList;
		
//		return propertyRepository.getWithinRange(queryParams.getMinBudget(), queryParams.getMaxBudget(),new Pageable() {
//			
//			@Override
//			public Pageable previousOrFirst() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public Pageable next() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public boolean hasPrevious() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public Sort getSort() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public int getPageSize() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//			
//			@Override
//			public int getPageNumber() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//			
//			@Override
//			public long getOffset() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//			
//			@Override
//			public Pageable first() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		});
		//return propertyRepository.sortByDistance(lat, lng);
		
	}
		
}