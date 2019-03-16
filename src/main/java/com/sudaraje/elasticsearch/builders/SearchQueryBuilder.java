package com.sudaraje.elasticsearch.builders;

import com.sudaraje.elasticsearch.model.Property;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchQueryBuilder {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public List<Property> getAll(String text) {

        QueryBuilder query = QueryBuilders.boolQuery()
                .should(
                        QueryBuilders.queryStringQuery(text)
                                .lenient(true)
                                .field("budget")
                ).should(QueryBuilders.queryStringQuery("*" + text + "*")
                        .lenient(true)
                        .field("budget")
                        .field("bedroom"));

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        List<Property> properties = elasticsearchTemplate.queryForList(build, Property.class);

        return properties;
    }
}
