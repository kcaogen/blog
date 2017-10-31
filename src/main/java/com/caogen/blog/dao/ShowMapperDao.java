package com.caogen.blog.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ShowMapperDao {
	
	List<HashMap<String,Object>> findAllCity();
	
	void updateCityCount(String cityName);
	
}
