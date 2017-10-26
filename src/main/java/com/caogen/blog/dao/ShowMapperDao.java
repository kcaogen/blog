package com.caogen.blog.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ShowMapperDao {
	
	public List<HashMap<String,Object>> findAllCity();
	
	public void updateCityCount(String cityName);
	
}
