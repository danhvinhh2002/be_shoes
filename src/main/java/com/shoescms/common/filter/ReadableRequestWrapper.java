package com.shoescms.common.filter;


import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ReadableRequestWrapper extends HttpServletRequestWrapper { // 상속
    private final Charset encoding;
    private byte[] rawData;
    private Map<String, String[]> params = new HashMap<>();

    public ReadableRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap()); // 원래의 파라미터를 저장

        String charEncoding = request.getCharacterEncoding(); // 인코딩 설정
        this.encoding = StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        try {
            InputStream is = request.getInputStream();
            this.rawData = IOUtils.toByteArray(is); // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성

            // body 파싱
            String collect = this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (StringUtils.isEmpty(collect)) { // body 가 없을경우 로깅 제외
                is.close();
                return;
            }
            if (request.getContentType() != null && request.getContentType().contains(
                    ContentType.MULTIPART_FORM_DATA.getMimeType())) { // 파일 업로드시 로깅제외

                is.close();
                return;
            }
            JSONParser jsonParser = new JSONParser();
            Object parse = jsonParser.parse(collect);
            if (parse instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray)parse;
                setParameter("requestBody", jsonArray.toJSONString());
            } else {
                JSONObject jsonObject = (JSONObject)parse;
                Iterator iterator = jsonObject.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String)iterator.next();
                    if (key != null) {
                        if (jsonObject.get(key) != null)
                            setParameter(key, jsonObject.get(key).toString().replace("\"", "\\\""));
                    }
                }
            }
        } catch (Exception e) {
            log.error("ReadableRequestWrapper ", e);
        }
    }

    @Override
    public String getParameter(String name) {
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            return paramArray[0];
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] dummyParamValue = params.get(name);

        if (dummyParamValue != null) {
            result = new String[dummyParamValue.length];
            System.arraycopy(dummyParamValue, 0, result, 0, dummyParamValue.length);
        }
        return result;
    }

    public void setParameter(String name, String value) {
        String[] param = {value};
        setParameter(name, param);
    }

    public void setParameter(String name, String[] values) {
        params.put(name, values);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Do nothing
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }
}
