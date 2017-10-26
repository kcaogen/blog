package com.caogen.blog.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.caogen.blog.dao.ShowMapperDao;
import com.caogen.blog.util.GeoCoord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caogen.blog.service.ShowService;

@Service
@Transactional
public class ShowServiceImpl implements ShowService {

	@Resource
	private ShowMapperDao showMapperDao;
	
	@Override
	public HashMap<String, Object> getAllCity() {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		List<HashMap<String,Object>> cityList = showMapperDao.findAllCity();
		HashMap<String,Double[]> cityMap = new HashMap<>();
		String name;
		for(int i = 0;i<cityList.size();i++){
			name = cityList.get(i).get("name").toString();
			cityMap.put(name, GeoCoord.getInstance().getCoordMap(name));
		}
		map.put("cityList", cityList);
		map.put("cityMap", cityMap);
		return map;
	}

	@Override
	public void updateCityCount(String cityName) {
		showMapperDao.updateCityCount(cityName);
	}

}
