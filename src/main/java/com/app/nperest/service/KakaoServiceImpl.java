package com.app.nperest.service;

import com.app.nperest.domain.MemberVO;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Primary
public class KakaoServiceImpl implements KakaoService {
    @Override
    public String getKakaoAccessToken(String code) {
        String accessToken = null;
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=80ff4cebe2960664dd2a8edf84078416"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:10000/kakao/login"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            if(connection.getResponseCode() == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                String result = "";

                while((line = br.readLine()) != null){
                    result += line;
                }

                JsonElement element = JsonParser.parseString(result);
                accessToken = element.getAsJsonObject().get("access_token").getAsString();

                br.close();
                bw.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return accessToken;
    }

    @Override
    public Optional<MemberVO> getKakaoInfo(String token) {
        String requestURL = "https://kapi.kakao.com/v2/user/me";
        MemberVO memberVO = null;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + token);

            if(connection.getResponseCode() == 200){
                memberVO = new MemberVO();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                String result = "";

                while((line = br.readLine()) != null){
                    result += line;
                }

                JsonElement element = JsonParser.parseString(result);
                JsonElement kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
                JsonElement profile = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject();

                memberVO.setKakaoEmail(kakaoAccount.getAsJsonObject().get("email").getAsString());
                memberVO.setMemberName(profile.getAsJsonObject().get("nickname").getAsString());
                memberVO.setKakaoProfileUrl(profile.getAsJsonObject().get("thumbnail_image_url").getAsString());

                br.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(memberVO);
    }
}
