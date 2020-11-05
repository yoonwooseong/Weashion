# Weashion
> 현재 위치에 대한 날씨에 따른 옷을 추천해주는 어플리케이션을 만들고자 함.

Android Studio (Android 8.1 (Oreo))(JAVA)를 사용해 현재 위치에 대한 날씨와 선호하는 스타일을 조사해 사용자에게 오늘 뭐입을지에 대한 도움을 주고자하는 프로젝트이다.
OPEN API는 Open Weather Map API, NAVER 검색 쇼핑 API를 사용하였고 라이브러리는 Swipe Menu list View, Glide(이미지 로딩), Ted permission(권한) 라이브러리를 이용했다.

![image](https://user-images.githubusercontent.com/57824259/87865507-50c98580-c9b1-11ea-8a8a-a342742edc6c.png)

## 설치 방법

언어 : JAVA
프로그램 : Android Studio (Android 8.1 (Oreo)) 

## 기획 의도

날씨에 따른 의상 추천
선호 의상 분석
구매 유도
쇼핑몰과 제휴를 맺어 홍보

## 사용 예제
 - 권한 설정(Tedpermission Library 이용)  
![image](https://user-images.githubusercontent.com/57824259/88687466-42a70200-d133-11ea-937d-578e8677fc66.png)
![image](https://user-images.githubusercontent.com/57824259/88687552-610cfd80-d133-11ea-9c55-75694894aa22.png)  

 - 선호 의상 분석  
 Dialog 생성 후 SharedPreference로 선택한 데이터들 저장  
![image](https://user-images.githubusercontent.com/57824259/88690156-4ee08e80-d136-11ea-885a-a1d8321be4aa.png)
![image](https://user-images.githubusercontent.com/57824259/88690165-5142e880-d136-11ea-8c25-99b1e280f8e2.png)  

 - 서랍 내 날씨 및 장바구니 기능  
 GPS를 통한 현재 위치의 날씨(오늘/ 내일/ 모래) 정보 및 날씨에 따른 배경 변경   
![image](https://user-images.githubusercontent.com/57824259/88691782-42f5cc00-d138-11ea-892e-f26c55713b82.png)
![image](https://user-images.githubusercontent.com/57824259/88691786-4426f900-d138-11ea-806a-d20cfe88c902.png)
![image](https://user-images.githubusercontent.com/57824259/88691793-45f0bc80-d138-11ea-81b3-06191e5acde4.png)

 - 내부 저장소 - 장바구니 데이터 저장 기능  
 장바구니에 저장된 목록과 해당 데이터들을 weashion / cart.txt 에 저장   
![image](https://user-images.githubusercontent.com/57824259/88692241-d0d1b700-d138-11ea-8e2f-23a3b3b9a3c6.png)
![image](https://user-images.githubusercontent.com/57824259/88692247-d202e400-d138-11ea-8dd2-a2a92f626de4.png)
![image](https://user-images.githubusercontent.com/57824259/88692257-d3341100-d138-11ea-9df3-3a4a7668a8b4.png)

 - 추천 의상 리스트  
![image](https://user-images.githubusercontent.com/57824259/88692355-f1017600-d138-11ea-85d5-21f3965d38ac.png)
![image](https://user-images.githubusercontent.com/57824259/88692323-e9da6800-d138-11ea-9a6c-4eb1f04668aa.png)
![image](https://user-images.githubusercontent.com/57824259/88692337-ed6def00-d138-11ea-8d41-f26f2910a3fa.png)

왼쪽 상단 – 날씨에 따른 아이콘 보이기
오른쪽 상단 – 추천된 제품 보기 방식 ( list / model )
하단 – 추천된 상품들을 다시 추천 받기 버튼
     – 통합 검색 버튼
     – 선호하는 스타일 재설정 버튼
        
상품 이미지 클릭 시 해당 카테고리 리스트 뷰 출력 
리스트 뷰 다음 버튼 클릭 시 다른 제품들 추천
하단 추천 의상 버튼 클릭 시 랜덤으로 추천


## 업데이트 내역
* 0.1.0  
    * 프로젝트 완성
* 0.0.0
    * prototype 완성

## 정보

 yoonwooseong – dntjd851@naver.com

[https://github.com/yoonwooseong/github-link](https://github.com/yoonwooseong/)

## 기여 방법

1. (<https://github.com/yourname/yourproject/fork>)을 포크합니다.
2. (`git checkout -b feature/fooBar`) 명령어로 새 브랜치를 만드세요.
3. (`git commit -am 'Add some fooBar'`) 명령어로 커밋하세요.
4. (`git push origin feature/fooBar`) 명령어로 브랜치에 푸시하세요. 
5. 풀리퀘스트를 보내주세요.

<!-- Markdown link & img dfn's -->
[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki
