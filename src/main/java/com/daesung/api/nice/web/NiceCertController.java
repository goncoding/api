//package com.daesung.api.nice.web;
//
//import com.daesung.api.nice.domain.NiceInfo;
//import com.daesung.api.nice.repository.NiceInfoRepository;
//import com.daesung.api.nice.web.dto.NiceCallResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.security.MessageDigest;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.crypto.Cipher;
//import javax.crypto.Mac;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//
//
//@RestController
//@RequestMapping("/niceCert")
//public class NiceCertController {
//
//    private final String CLIENT_SECRET = "44f4b177a9484a29a53dc3fc8f8955dc";
//    private final String CLIENT_ID = "e52677b6-2bbd-4cac-9f48-5318cb8ec0f8";
//    private final String PRODUCT_ID = "2101979031";
//    private final String redirectUrl =  "http://192.168.14.99:8080";
//
//    @Autowired
//    private NiceInfoRepository niceInfoRepository;
//
////    @GetMapping("/callback")
////    public ResponseEntity niceCallback() {
////
////        return ResponseEntity.ok("nice call back 성공");
////    }
//
//    /**
//     *  ACCESS Token 발급
//     */
//    @PostMapping(value = "/getNiceCryptoToken")
//    public ResponseEntity getNiceCryptoToken() throws Exception {
//
//        String vUrl = "https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token";
//        //-----------------------------------------------------------------
//        // 1. 기관토큰 GET
//        //-----------------------------------------------------------------
//        String access_token = getAccessToken(); // 기관토큰(50년 유효- 이상시 패기후 재발급)
//        //-----------------------------------------------------------------
//        // 2. 암호화 토큰 GET
//        //-----------------------------------------------------------------
//        long current_timestamp = new Date().getTime()/1000;
//
//        String auth = access_token+":"+current_timestamp+":"+CLIENT_ID;
//        //byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
//        //String authStringEnc = new String(authEncBytes);
//        String authStringEnc = Base64.getEncoder().encodeToString(auth.getBytes());
//
//
//        JSONObject headerSet = new JSONObject();
//        headerSet.put("CNTY_CD", "ko");
//
//        JSONObject bodySet = new JSONObject();
//
//        //ex "req_dtim": "20210622162600"
//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String vDtlDtime = now.format(dtf);
//
//        //"req_no":"REC_b31215e48ade47c2"
//        String vReqNo = UUID.randomUUID().toString().replaceAll("-", "");
//        vReqNo = "REC_"+ vReqNo.substring(0, 16); //20자리 고유번호
//
//        bodySet.put("req_dtim", vDtlDtime); //요청일시 (YYYYMMDDHH24MISS)
//        bodySet.put("req_no", vReqNo); // 거래고유번호
//        bodySet.put("enc_mode", "1"); //사용할 암복호화 구분 >> 1 : AES128/CBC/PKCS7
//
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("dataHeader", headerSet);
//            obj.put("dataBody", bodySet);
//        } catch(JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println("암호화 토큰 요청 request = " + obj.toString());
//
//
//        HttpURLConnection conn = null;
//        StringBuffer outResult = new StringBuffer();
//        try {
//            URL url = new URL(vUrl);
//            //URL객체 가져오기
//            conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoOutput(true);  // InputStream으로 서버로 부터 응답을 받겠다는 옵션
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Authorization", "bearer "+authStringEnc);
//            conn.setRequestProperty("ProductID", PRODUCT_ID);
//
//            // BufferedWriter 객체 생성
////            BufferedWriter bw
////                    = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("파일경로" + "파일이름.csv"), "인코딩 형식"));
////
////            bw.write("서버 IP,서버 계정,서버 비밀번호"); // 파일 내부에 쓰고자 하는 내용
////            bw.newLine(); //개행
////
////            bw.flush(); // 버퍼 비우기
////            bw.close(); // BufferedWriter 닫기
//
//
//            // Request Body에 Data를 담기위해 OutputStream 객체를 생성.
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            // Request Body에 Data 셋팅.
//            bw.write(obj.toString());
//            // Request Body에 Data 입력.
//            bw.flush();
//            // TimeOut 시간 (Read시 연결 시간)
//            conn.setReadTimeout(10000);
//            // TimeOut 시간 (서버 접속시 연결 시간)
//            conn.setConnectTimeout(10000);
//            // 실제 서버로 Request 요청 하는 부분. (응답 코드를 받는다. 200 성공, 나머지 에러)
//            int responseCode = conn.getResponseCode();
//
//            String inputList = null;
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            while( (inputList = in.readLine()) != null) {
//                outResult.append(inputList);
//            }
//            System.out.println("암호화 토큰 응답 response = " +outResult.toString());
//
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }finally {
//            conn.disconnect();
//        }
//
//        //-----------------------------------------------------------------
//        // 3. 대칭키 생성 (비밀키 생성) 예를들면 k = 01011001
////        보내고자하는 전송메시지(m = 10110011)를 밥과 약속했던 비밀키(k = 01011001)로 XOR 계산해 암호화한다.
//        //전송메세지(m)를 대칭키(k)로 XOR 계산해, c = 11101010 라는 암호화 된 메세지가 만들어지면, 이를 전송한다.
//        //이러한 방법을 통해, 중간에 해커들은 암호화 된 메세지(c=11101010)를 가로챈다고 한들, 전송메세지(m=10110011)를 유추할 수 없게 된다
//        //하지만, 밥은 암호화 된 메세지(c)로부터 전송메세지(m)를 쉽게 복호화 할 수 있다.(엘리스와 비밀키(k)를 공유하고 있기 때문이다.)
//        //따라서, 밥은 암호화 된 메세지(c)를 비밀키(k)로 XOR 계산해 전송메세지(m)를 복호화한다.
//        //-----------------------------------------------------------------
//        System.out.println(outResult.toString());
//        JSONObject jsonObject = new JSONObject(outResult.toString());
//        JSONObject dataBody = jsonObject.getJSONObject("dataBody");
//
//        String vTokenVal = dataBody.get("token_val").toString();
//        String vSiteCode = dataBody.get("site_code").toString();
//        String vTokenVersionId = dataBody.get("token_version_id").toString();
//
//        String vAuthtype = "M"; //인증수단 - M:휴대폰인증
//
//        //Sha256 및 base64 encoding
//        String value = vDtlDtime.trim() + vReqNo.trim() + vTokenVal.trim();
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(value.getBytes());
//        byte[] arrHashValue = md.digest();
//        String resultVal = Base64.getEncoder().encodeToString(arrHashValue);//Base64Util.base64Encode(arrHashValue);
//
//
//        String key = resultVal.substring(0,16);  // 데이터 암호화할 대칭키
//        String iv = resultVal.substring(resultVal.length()-16, resultVal.length());  // 데이터암호화할 Initail Vector
//
//        String hmac_key = resultVal.substring(0,32); //무결성키 : 암호화값 위변조 체크용
//
//        System.out.println("3. 대칭키 생성 "+"resultVal : "+resultVal + ", key "+key + ", iv : "+ iv + ", hmac_key : "+ hmac_key);
//        System.out.println("key = " + key); //대칭키
//        System.out.println("iv = " + iv); //초기화 백터 : 동일한 평문이라도 항상 다른 암호문으로 암호화
//        System.out.println("hmac_key = " + hmac_key); //암호값 위변조 체크용
//
//        //-----------------------------------------------------------------
//        // 4. 요청데이터 암호화
//        //-----------------------------------------------------------------
//        JSONObject reqSet = new JSONObject();
//        reqSet.put("returnurl", redirectUrl + "?keyNo="+vReqNo);
//        reqSet.put("sitecode", vSiteCode);
//        reqSet.put("popupyn", "Y");
//        reqSet.put("authtype", vAuthtype);
//        reqSet.put("methodtype", "post");
//        reqSet.put("receivedata", vReqNo); // 고유번호 넘겨보기!!
//
//        //String reqData = {"returnurl":"https://서비스도메인/XXX/XXX","sitecode":"XXXX", "popupyn":"Y","receivedata":"xxxxdddeee"};
//        String reqData = reqSet.toString();
//
//        System.out.println("4. 요청데이터 암호화 reqData = " + reqData);
//
//        //자바에서 암호화, 복호화 기능을 제공하는 Cipher 클래스
//        // 암호화 (enc_data)
//        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//
//        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
//        byte[] encrypted = cipher.doFinal(reqData.trim().getBytes());
//        //String enc_data = Base64Util.encode(encrypted, false);
//        String enc_data = Base64.getEncoder().encodeToString(encrypted);
//
//        // Hmac 무결성체크값(integrity_value)
//        byte[] hmacSha256 = hmac256(hmac_key.getBytes(), enc_data.getBytes());
//        String integrity_value = Base64.getEncoder().encodeToString(hmacSha256);
//
//        //-----------------------------------------------------------------
//        // 5. 리턴할 데이터 생성!!
//        //-----------------------------------------------------------------
//        //복호화에 사용될 key 저장!!
//        Map<String, Object> insertMap = new HashMap<String, Object>();
//        insertMap.put("compCode", "100");
//        insertMap.put("reqNo", vReqNo);////"req_no":"REC_b31215e48ade47c2" 요청 고유넘버
//
//        //대칭키
//        insertMap.put("key", key); // 데이터 암호화할 대칭키 (앞에서 16자리)
//        insertMap.put("iv", iv);// 데이터암호화할 Initail Vector (뒤에서 16자리)
//        insertMap.put("hmacKey", hmac_key);//무결성키 : 암호화값 위변조 체크용 (32자리)
//
//        NiceInfo niceInfo = NiceInfo.builder()
//                .compCode("100")
//                .reqNo(vReqNo)
//                .symKey(key)
//                .iv(iv)
//                .hmacKey(hmac_key)
//                .build();
//
//        //todo 위의 복화화 key는 db에 저장
//        NiceInfo savedNiceInfo = niceInfoRepository.save(niceInfo);
//
//        NiceCallResponse callResponse = NiceCallResponse.builder()
//                .token_version_id(vTokenVersionId)
//                .enc_data(enc_data)
//                .integrity_value(integrity_value)
//                .build();
//
//        System.out.println("callResponse.getToken_version_id() = " + callResponse.getToken_version_id());
//        System.out.println("callResponse.getEnc_data() = " + callResponse.getEnc_data());
//        System.out.println("callResponse.setIntegrity_value() = " + callResponse.getIntegrity_value());
//
//        //todo 아래의 return data는 응답값으로 보내주기
//        //todo 3개의 값을 post로 전송했을때 본인인증 창 뜨게된다..
//        // 리턴데이터 생성 ------------------------------------------- // form에서 post로 보내줄 데이터 json으로 복호화로 보내줄 데이터...
////        Map<String, Object> resultMap = new HashMap<String, Object>();
////        resultMap.put("token_version_id", vTokenVersionId); //암호화토큰요청_API 응답으로 받은 값
////        resultMap.put("enc_data", enc_data); //암호화한 요청정보
////        resultMap.put("integrity_value", integrity_value); //enc_data의 무결성 값
//
//
//        return ResponseEntity.ok(callResponse);
//    }
//
////    @ApiOperation(value = "(2) 나이스 인증결과 복호화", notes = "나이스 인증결과 복호화")
////    @ApiImplicitParams({
////            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
////    })
////    @PostMapping(value = "/getNiceDeSet")
////    public SingleResult<Map<String, Object>> getNiceDeSet(@RequestBody Map<String, Object> appInfo) throws Exception {
////
////        //post 값으로 appInfo가 들어옴
//////        resultMap.put("token_version_id", vTokenVersionId);
//////        resultMap.put("enc_data", enc_data);
//////        resultMap.put("integrity_value", integrity_value);
////        //해당 부분 json으로 보내면 될듯...
////
////        Map<String, Object> resultMap = new HashMap<String, Object>();
////        resultMap = niceCertService.getCertKey(appInfo);
////
////        String encData = (String) appInfo.get("encData"); //form에서 보낸 값 가져오기
////
////        String key =  resultMap.get("key").toString(); //요청 시 암호화한 key와 동일
////        String iv =  resultMap.get("iv").toString(); //요청 시 암호화한 iv와 동일
////
////        // 복호화
////        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
////        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
////        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
////        //Base64.getEncoder().encodeToString(hmacSha256);
////        byte[] cipherEnc = Base64.getDecoder().decode(encData); //Base64Util.decode(base64Enc);
////        String resData =   new String(c.doFinal(cipherEnc), "euc-kr");
////
////        Map<String, Object> rtnMap = new HashMap<String, Object>();
////        rtnMap.put("decData", resData);
////
////        return responseService.getSingleResult(rtnMap);
////    };
//
//
//    // Hmac ---------------------------------------------------------
//    public static byte[] hmac256(byte[] secretKey,byte[] message){
//        byte[] hmac256 = null;
//
//        try {
//            Mac mac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
//            mac.init(sks);
//            hmac256 = mac.doFinal(message);
//            return hmac256;
//
//        } catch(Exception e){
//
//            throw new RuntimeException("Failed to generate HMACSHA256 encrypt");
//
//        }
//    }
//
//
//    private String getAccessToken() {
//        String vAccess_token = null;
//        String vUrl = "https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token";
//        String auth = CLIENT_ID+":"+CLIENT_SECRET;
//        String authStringEnc = Base64.getEncoder().encodeToString(auth.getBytes());
//
//        // HttpURLConnection 객체 생성.
//        HttpURLConnection conn = null;
//        StringBuffer outResult = new StringBuffer();
//        try {
//            URL url = new URL(vUrl);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//            conn.setRequestProperty("Authorization", "Basic "+authStringEnc);
//
//            //BufferedWriter은 출력을 담당한다. 자바에서 흔히 쓰는 System.out.println();을 대체 / 사용하고 나서는 버퍼를 적용하고 비워주고 닫아야 한다.
//            //OutputStreamWriter은 BufferedWriter와 짝꿍
//            //BufferedReader의 경우 InputStreamReader와 짝꿍
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            StringBuilder sb = new StringBuilder();
//            sb.append("grant_type=client_credentials");
//            sb.append("&scope=default");
//            bw.write(sb.toString());
//            bw.flush();
//
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(10000);  // 10초 동안 기다린 후 응답이 없으면 종료
//
//            int responseCode = conn.getResponseCode(); //함수를 통해 요청 전송
//            String inputList = null;
//
//            // BufferedReader는 InputStreamReader를 입력받아 문자열을 출력
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            while( (inputList = in.readLine()) != null) {
//                outResult.append(inputList);
//            }
//            System.out.println(outResult.toString());
//
//            JSONObject jsonObject = new JSONObject(outResult.toString());
//            JSONObject dataBody = jsonObject.getJSONObject("dataBody");
//            //System.out.println(dataBody.get("access_token"));
//            vAccess_token = (String) dataBody.get("access_token");
//
//        } catch(Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            conn.disconnect();
//        }
//
//        return vAccess_token;
//    }
//
//
//}
