package com.sudaraje.elasticsearch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.sudaraje.elasticsearch.model.Property;

@Repository
public interface PropertyRepository extends ElasticsearchRepository<Property,String>{

	//@Query("{\"query\" : {\"must\" : {\"range\" : {\"budget\" : {\"from\" : \"27000\",\"to\" : \"3000\",\"include_lower\" : true,\"include_upper\" : true}}}}}")
	List<Property> findByBudgetBetween(@Param("minBudget")int minBudget, @Param("maxBudget")int maxBudget);
	
	List<Property> findByBudget(int budget);

			
}
