package com.caogen.blog.util;

import java.util.HashMap;

public class GeoCoord {

    private static HashMap<String, Double[]> geoCoordMap;

    private GeoCoord(){
        geoCoordMap = new HashMap<>();
        geoCoordMap.put("甘肃", new Double[]{103.73, 36.03});
        geoCoordMap.put("青海", new Double[]{101.74, 36.56});
        geoCoordMap.put("四川", new Double[]{104.06, 30.67});
        geoCoordMap.put("河北", new Double[]{114.48, 38.03});
        geoCoordMap.put("云南", new Double[]{102.73, 25.04});
        geoCoordMap.put("贵州", new Double[]{106.71, 26.57});
        geoCoordMap.put("湖北", new Double[]{114.31, 30.52});
        geoCoordMap.put("河南", new Double[]{113.65, 34.76});
        geoCoordMap.put("山东", new Double[]{117.00, 36.65});
        geoCoordMap.put("江苏", new Double[]{118.78, 32.04});
        geoCoordMap.put("安徽", new Double[]{117.27, 31.86});
        geoCoordMap.put("浙江", new Double[]{120.19, 30.26});
        geoCoordMap.put("江西", new Double[]{115.89, 28.68});
        geoCoordMap.put("福建", new Double[]{119.3, 26.08});
        geoCoordMap.put("广东", new Double[]{113.23, 23.16});
        geoCoordMap.put("湖南", new Double[]{113.00, 28.21});
        geoCoordMap.put("海南", new Double[]{110.35, 20.02});
        geoCoordMap.put("辽宁", new Double[]{123.38, 41.8});
        geoCoordMap.put("吉林", new Double[]{125.35, 43.88});
        geoCoordMap.put("黑龙江", new Double[]{126.63, 45.75});
        geoCoordMap.put("山西", new Double[]{112.53, 37.87});
        geoCoordMap.put("陕西", new Double[]{108.95, 34.27});
        geoCoordMap.put("台湾", new Double[]{121.30, 25.03});
        geoCoordMap.put("北京", new Double[]{116.46, 39.92});
        geoCoordMap.put("上海", new Double[]{121.48, 31.22});
        geoCoordMap.put("重庆", new Double[]{106.54, 29.59});
        geoCoordMap.put("天津", new Double[]{117.2, 39.13});
        geoCoordMap.put("内蒙古", new Double[]{111.65, 40.82});
        geoCoordMap.put("广西", new Double[]{108.33, 22.84});
        geoCoordMap.put("西藏", new Double[]{91.11, 29.97});
        geoCoordMap.put("宁夏", new Double[]{106.27, 38.47});
        geoCoordMap.put("新疆", new Double[]{87.68, 43.77});
        geoCoordMap.put("香港", new Double[]{114.17, 22.28});
        geoCoordMap.put("澳门", new Double[]{113.54, 22.19});
    }

    private static class GeoCoordHolder{
        private final static GeoCoord instance=new GeoCoord();
    }

    public static GeoCoord getInstance(){
        return GeoCoordHolder.instance;
    }

    public Double[] getCoordMap(String province) {
        return geoCoordMap.get(province);
    }

}
