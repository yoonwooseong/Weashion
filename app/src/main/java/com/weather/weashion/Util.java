package com.weather.weashion;

public interface Util {

    int SEARCH_COUNT = 20;
    int SEARCH_COUNT2 = 4;

    int TODAY = 0;
    int TOMORROW = 1;
    int AFTERTOMORROW = 2;

    /*위치*/
    String LAT = "37.554150";
    String LON = "126.935714";
    String APPID = "cd32ca6688958e1348f269b4e28ccde6";

    /*카테고리*/
    int CATEGORY_HAT = 0;
    int CATEGORY_TOP = 1;
    int CATEGORY_BOTTOM = 2;
    int CATEGORY_SHOES = 3;
    int CATEGORY_UMB = 4;
    int CATEGORY_ACC = 5;

    //검색 목록
    String[] hat = {"패션모자","스포츠헤어밴드","스냅백","비니"};
    String[] top = {"반팔티","긴팔티","맨투맨"};
    String[] top2 = {"얇은","두꺼운"};//여름, 겨울 한정.
    String[] tops = {"자켓","바람막이","코트"};//자켓류
    String[] bottom = {"반바지","면바지","청바지"};
    String[] shoes = {"나이키신발","아디다스신발","아식스신발","컨버스신발","닥터마틴신발"};
    String[] shoes2 = {"샌들","뮬","쪼리","3선슬리퍼"};//여름한정 +추가

}
