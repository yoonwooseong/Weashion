package com.weather.weashion;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Parsers {
    private String searchs = "";
    private int starts;

    public String Parser(int start, String search) {
        this.searchs = search;
        int count = Util.SEARCH_COUNT;
        this.starts = start;
        String apiURL = "https://openapi.naver.com/v1/search/shop.json?query=" + searchs + "&start=" + starts + "&display=" + count;

        Map<String, String> responseHeaders = new HashMap<>();

        //아이디 비번
        responseHeaders.put("X-Naver-Client-Id", "Mze67FDIaGt2lHrQl0Tf");
        responseHeaders.put("X-Naver-Client-Secret", "jdMkXeydSw");

        String responseBody = get(apiURL, responseHeaders);
        return responseBody;//이때 보냄.
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
        }
        catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        }
        finally {
            con.disconnect();
        }
    }//get : 키값 http에 송출준비

    private HttpURLConnection connect(String apiURL) {
        try {
            URL url = new URL(apiURL);
            return (HttpURLConnection) url.openConnection();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiURL, e);
        }
        catch (IOException e) {
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
        }
        catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }//readbody : 보낸 내용물을 확인 받고, 내용물 받기.
}