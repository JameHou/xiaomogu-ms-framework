package com.github.xiaomogu.web.utils;


import com.github.xiaomogu.commons.jackson.HzJson;
import com.github.xiaomogu.web.config.SpringContextConfig;
import com.github.xiaomogu.web.config.SpringMvcProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WebLogUtils {
    private static HzJson hzJson = SpringContextConfig.getBean(HzJson.class);
    private static SpringMvcProperties springMvcProperties = SpringContextConfig.getBean(SpringMvcProperties.class);
    public static String getAllHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        for (; headerNames.hasMoreElements(); ) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return hzJson.obj2string(headers);
    }

    public static String getRequestParams(HttpServletRequest request) {
        String re = hzJson.obj2string(request.getParameterMap());
        if (re.length() > 256) {
            return re.substring(0, 256);
        }
        return re;
    }

    public static String getSessionUsername(HttpServletRequest request, String key) {
        Object result = request.getSession()
                               .getAttribute(key);
        if (result == null)
            return "";
        return String.valueOf(result);
    }

    public static String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL()
                      .toString();
    }

    public static String getAllHeaders(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        headerNames.forEach(key -> headers.put(key, response.getHeaders(key)));
        return hzJson.obj2string(headers);
    }

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }


    public static boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType()
                .startsWith("multipart/form-data");
    }

    public static boolean isMultipart(final HttpServletResponse response) {
        return response.getContentType() != null && (response.getContentType()
                .startsWith("multipart/form-data") || response.getContentType()
                .startsWith("application/octet-stream"));
    }

    public static boolean isBinaryContent(final HttpServletRequest request) {
        if (request.getContentType() == null) {
            return false;
        }
        return request.getContentType().startsWith("image") || request.getContentType()
                .startsWith("video") || request.getContentType().startsWith("audio");
    }

    public static boolean isBinaryContent(final HttpServletResponse response) {
        return response.getContentType() != null && (response.getContentType()
                .startsWith("image") || response.getContentType().startsWith("video") || response
                .getContentType().startsWith("audio"));
    }


    private static final String DEFAULT_SKIP_PATTERN =
            "/api-docs.*|/actuator.*|/swagger.*|.*\\.png|.*\\.css|.*\\.js|.*\\.html|/favicon.ico|/hystrix.stream|/webjars|/csrf";

    private static final Pattern SKIP_PATTERNS = Pattern.compile(DEFAULT_SKIP_PATTERN);

    private static boolean noContain(HttpServletRequest request) {
        String path = request.getServletPath();
        return !SKIP_PATTERNS.matcher(path).matches();
    }

    public static boolean isNormalRequest(HttpServletRequest request) {
        return !isMultipart(request) && !isBinaryContent(request) && noContain(request);
    }


    public static String getPayLoad(byte[] buf, String characterEncoding) {
        String payload = "";
        if (buf == null) return payload;
        if (buf.length > 0) {
            int length = Math.min(buf.length, springMvcProperties.getPayloadLength());
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
        }
        return payload;
    }

}
