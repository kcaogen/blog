package com.caogen.blog.service;

import java.util.HashMap;

public interface ShowService {
	HashMap<String,Object> getAllCity();
	
	void updateCityCount(String cityName);
}
