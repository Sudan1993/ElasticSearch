package com.sudaraje.elasticsearch.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudaraje.elasticsearch.model.Property;
import com.sudaraje.elasticsearch.model.PropertyQueryParams;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchQueryBuilder {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	private ObjectMapper objectMapper;

	public SearchQueryBuilder(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	public List<Property> getAll(String text) {

		QueryBuilder query = QueryBuilders.boolQuery()
				.should(QueryBuilders.queryStringQuery(text).lenient(true).field("budget")).should(QueryBuilders
						.queryStringQuery("*" + text + "*").lenient(true).field("budget").field("bedroom"));

		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(query).build();

		List<Property> properties = elasticsearchTemplate.queryForList(build, Property.class);

		return properties;
	}

	private List<Property> getSearchResult(SearchResponse response) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Property> propertyDocuments = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			propertyDocuments.add(objectMapper.convertValue(hit.getSourceAsMap(), Property.class));
		}

		return propertyDocuments;
	}

	public List<Property> customSearch(PropertyQueryParams queryParams) {
		// get the post params
		double lat = queryParams.getLat();
		double lng = queryParams.getLng();
		int minBudget = queryParams.getMinBudget();
		int maxBudget = queryParams.getMaxBudget();
		int minBedroom = queryParams.getMinBedroom();
		int maxBedroom = queryParams.getMaxBedroom();
		int minBathroom = queryParams.getMinBathroom();
		int maxBathroom = queryParams.getMaxBathroom();

		// Either min or max should be present for sure -- set up the values for min and
		// max for all params
		if (minBudget == 0) {
			minBudget = (int) (maxBudget * 0.75);
			maxBudget = (int) (maxBudget * 1.25);
		} else if (maxBudget == 0) {
			minBudget = (int) (minBudget * 0.75);
			maxBudget = (int) (minBudget * 1.25);
		} else if (minBedroom == 0) {
			minBedroom = (int) (maxBedroom - 2);
			maxBedroom = (int) (maxBedroom + 2);
		} else if (maxBedroom == 0) {
			minBedroom = (int) (minBedroom - 2);
			maxBedroom = (int) (minBedroom + 2);
		} else if (minBathroom == 0) {
			minBathroom = (int) (maxBathroom - 2);
			maxBathroom = (int) (maxBathroom + 2);
		} else if (maxBathroom == 0) {
			minBathroom = (int) (minBathroom - 2);
			maxBathroom = (int) (minBathroom + 2);
		}
		
		QueryBuilder geoDistanceQueryBuilder = QueryBuilders
	            .geoDistanceQuery("pin.location")
	            .point(lat, lng)
	            .distance(10, DistanceUnit.MILES);

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery()
				.must(QueryBuilders.matchAllQuery())
				//.filter(geoDistanceQueryBuilder)
				.must(QueryBuilders.rangeQuery("budget").to(maxBudget).from(minBudget))
				.must(QueryBuilders.rangeQuery("bathroom").to(maxBathroom).from(minBathroom))
				.must(QueryBuilders.rangeQuery("bedroom").to(maxBedroom).from(minBedroom)));
		searchSourceBuilder.size(500);
		searchRequest.source(searchSourceBuilder);
		searchRequest.scroll();
		searchRequest.preference("_local");
		SearchResponse response = null;
		try {
			response = restHighLevelClient.search(searchRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSearchResult(response);

		/*
		 * QueryBuilder query = QueryBuilders.boolQuery()
		 * .must(QueryBuilders.rangeQuery("budget").to(maxBudget).from(minBudget));
		 * .must(QueryBuilders.rangeQuery("bathroom").to(maxBathroom).from(minBathroom))
		 * .must(QueryBuilders.rangeQuery("bedroom").to(maxBedroom).from(minBedroom));
		 * 
		 * SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		 * sourceBuilder.query(query); sourceBuilder.from(0); sourceBuilder.size(500);
		 * 
		 * NativeSearchQuery build = new
		 * NativeSearchQueryBuilder().withQuery(query).build(); List<Property>
		 * properties = elasticsearchTemplate.queryForList(build, Property.class);
		 * return properties;
		 */
	}
}
