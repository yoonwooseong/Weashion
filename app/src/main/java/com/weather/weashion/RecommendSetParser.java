package com.weather.weashion;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecommendSetParser {
    SearchVO searchVO;
    int start;

    public SearchVO RecommendSetParser(String query, int categoryNum) {

        int count = 1;
        int num = new Random().nextInt(10-1);//다른곳 start값 바꾸면 다른 어뎁터는 수정해야함//이거 갯수늘리면 오류 없을수잇어서
        this.start = num;
        String apiURL = "https://openapi.naver.com/v1/search/shop.json?query=" + query + "&start=" + start + "&display=" + count;

        Map<String, String> responseHeaders = new HashMap<>();

        //아이디 비번
        responseHeaders.put("X-Naver-Client-Id", "Mze67FDIaGt2lHrQl0Tf");
        responseHeaders.put("X-Naver-Client-Secret", "jdMkXeydSw");

        String responseBody = get(apiURL, responseHeaders);

        JSONObject jObject;
        JSONObject resultObject = null;
        JSONObject itemInfo = null;
        Log.i("MY", "여기까진" + responseBody);

        try {
            jObject = new JSONObject(responseBody);

            searchVO = new SearchVO();
            itemInfo = (JSONObject) jObject.getJSONArray("items").get(0);
            Log.i("err",responseBody);

            String title = itemInfo.getString("title");
            Pattern pattern = Pattern.compile("<.*?>");
            Matcher matcher = pattern.matcher( title );

            if( matcher.find() ){
                String s_title = matcher.replaceAll("");//제거
                searchVO.setTitle(s_title);; //b태그가 제거된 타이틀
            } else {
                searchVO.setTitle(title);
            }
            searchVO.setLink(itemInfo.getString("link"));
            searchVO.setImage(itemInfo.getString("image"));
            searchVO.setLprice(itemInfo.getString("lprice"));
            searchVO.setBrand(itemInfo.getString("brand"));
            searchVO.setBrand(itemInfo.getString("maker"));
            searchVO.setMaker(itemInfo.getString("maker"));
            searchVO.setType(categoryNum);

            Log.i("MY", "이름" + searchVO.getTitle());
            Log.i("MY", "링크" + searchVO.getLink());
            Log.i("MY", "이미지" + searchVO.getImage());
            Log.i("MY", "가격" + searchVO.getLprice());
            Log.i("MY", "브랜드" + searchVO.getBrand());
            Log.i("MY", "메이커" + searchVO.getMaker());

            return searchVO;//이때 보냄.
        } catch (Exception e) {
            e.printStackTrace();
            searchVO = new SearchVO();
            searchVO.setTitle("준비중");
            searchVO.setBrand(" ");
            searchVO.setLprice(" ");
            searchVO.setImage(" ");
            searchVO.setType(categoryNum);
            return searchVO;
        }
    }

    private String get(String apiURL, Map<String, String> responseHeaders) {
        HttpURLConnection con = connect(apiURL);
        
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : responseHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }//get : 키값 http에 송출준비

    private HttpURLConnection connect(String apiURL) {
        try {
            URL url = new URL(apiURL);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiURL, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiURL, e);
        }
    }//connect : String타입 url http에 올리기.

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);

            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }//readbody : 보낸 내용물을 확인 받고, 내용물 받기.
}
