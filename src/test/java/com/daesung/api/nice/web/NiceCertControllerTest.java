package com.daesung.api.nice.web;

import com.daesung.api.common.BaseControllerTest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class NiceCertControllerTest extends BaseControllerTest {


    private final String CLIENT_SECRET = "44f4b177a9484a29a53dc3fc8f8955dc";
    private final String CLIENT_ID = "e52677b6-2bbd-4cac-9f48-5318cb8ec0f8";
    private final String PRODUCT_ID = "2101979031";

    @DisplayName("")
    @Test
    public void _테스트01() throws Exception{



    }

//    public static void main(String[] args) {
//        String enc_data = "SDFSADFSADFSDF=";
//        byte[] hmacSha256 = hmac256(hmac_key.getBytes(), enc_data.getBytes());
//        String integrity_value = Base64.getEncoder().encodeToString(hmacSha256);
//    }
//
//    public static byte[] hmac256(byte[] secretKey,byte[] message){
//        byte[] hmac256 = null;
//        try{
//            Mac mac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
//            mac.init(sks);
//            hmac256 = mac.doFinal(message);
//            return hmac256;
//        } catch(Exception e){
//            throw new RuntimeException("Failed to generate HMACSHA256 encrypt");
//        }
//    }

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

//        String accessToken = getAccessToken();
//        System.out.println("accessToken = " + accessToken);


        String vReqNo = UUID.randomUUID().toString().replaceAll("-", "");
        vReqNo = "REC_"+ vReqNo.substring(0, 16);

        System.out.println("vReqNo = " + vReqNo);


    }

    //access_token요청. >> 1회발급, 50년 사용
    private String getAccessToken() {
        String vAccess_token = null;
        String vUrl = "https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token";
        String auth = CLIENT_ID+":"+CLIENT_SECRET;
        String authStringEnc = Base64.getEncoder().encodeToString(auth.getBytes());

        // HttpURLConnection 객체 생성.
        HttpURLConnection conn = null;
        StringBuffer outResult = new StringBuffer();
        try {
            URL url = new URL(vUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic "+authStringEnc);

            //BufferedWriter은 출력을 담당한다. 자바에서 흔히 쓰는 System.out.println();을 대체 / 사용하고 나서는 버퍼를 적용하고 비워주고 닫아야 한다.
            //OutputStreamWriter은 BufferedWriter와 짝꿍
            //BufferedReader의 경우 InputStreamReader와 짝꿍
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=client_credentials");
            sb.append("&scope=default");
            bw.write(sb.toString());
            bw.flush();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);  // 10초 동안 기다린 후 응답이 없으면 종료

            int responseCode = conn.getResponseCode(); //함수를 통해 요청 전송
            String inputList = null;

            // BufferedReader는 InputStreamReader를 입력받아 문자열을 출력
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while( (inputList = in.readLine()) != null) {
                outResult.append(inputList);
            }
            System.out.println(outResult.toString());

            JSONObject jsonObject = new JSONObject(outResult.toString());
            JSONObject dataBody = jsonObject.getJSONObject("dataBody");
            //System.out.println(dataBody.get("access_token"));
            vAccess_token = (String) dataBody.get("access_token");

        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            conn.disconnect();
        }

        return vAccess_token;
    }


}