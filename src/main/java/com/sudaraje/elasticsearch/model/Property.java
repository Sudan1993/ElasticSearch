package com.sudaraje.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName="propertydata",type="properties",shards=2)
public class Property{
	
	@Id
	private String id;
	private double lat;
	private double lng;
	private int bedroom;
	private int bathroom;
	public int budget;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public int getBedroom() {
		return bedroom;
	}
	public void setBedroom(int bedroom) {
		this.bedroom = bedroom;
	}
	public int getBathroom() {
		return bathroom;
	}
	public void setBathroom(int bathroom) {
		this.bathroom = bathroom;
	}
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	@Override
	public String toString() {
		return "Property [id=" + id + ", lat=" + lat + ", lng=" + lng + ", bedroom=" + bedroom + ", bathroom="
				+ bathroom + ", budget=" + budget + "]";
	}
	public Property(String id, double lat, double lng, int bedroom, int bathroom, int budget) {
		super();
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.bedroom = bedroom;
		this.bathroom = bathroom;
		this.budget = budget;
	}
	public Property() {

	}
	
	
	
}