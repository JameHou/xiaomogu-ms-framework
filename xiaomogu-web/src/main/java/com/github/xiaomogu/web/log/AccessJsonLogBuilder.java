package com.github.xiaomogu.web.log;

import com.github.xiaomogu.commons.jackson.HzJson;
import com.github.xiaomogu.commons.jackson.RespResultCode;
import com.github.xiaomogu.web.config.SpringMvcProperties;
import com.github.xiaomogu.web.utils.IpUtil;
import com.github.xiaomogu.web.utils.WebLogUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

/**
 * @auther JameHou
 * @date 2019/1/5 17:22
 */
@Data
@Slf4j
public class AccessJsonLogBuilder {

    private HzJson hzJson;
    private SpringMvcProperties springMvcProperties;
    private Map<String, Object> data;

    public AccessJsonLogBuilder(HzJson hzJson, SpringMvcProperties springMvcProperties) {
        this.hzJson = hzJson;
        this.springMvcProperties = springMvcProperties;
        data = Maps.newHashMap();
        put("_TIME_", Instant.now().atZone(ZoneId.of("+08:00")).toString());
    }

    public static AccessJsonLogBuilder accessJsonLogBuilder(HzJson hzJson,
                                                            SpringMvcProperties springMvcProperties) {
        return new AccessJsonLogBuilder(hzJson, springMvcProperties);
    }

    public AccessJsonLogBuilder put(String key, Object value) {
        data.put(key, value);
        return this;
    }


    public AccessJsonLogBuilder put(HttpServletRequest request) {
        this.put("Q_METHOD", request.getMethod())
                .put("Q_URL", WebLogUtils.getRequestUrl(request))
                .put("Q_PARAM", WebLogUtils.getRequestParams(request))
                .put("Q_HEADERS", WebLogUtils.getAllHeaders(request))
                .put("Q_IP", IpUtil.getRemoteAddress(request));
        return this;
    }

    public AccessJsonLogBuilder put(HttpServletResponse response) {
        this.put("P_HEADERS", WebLogUtils.getAllHeaders(response))
                .put("P_CODE", response.getStatus());
        return this;
    }

    public AccessJsonLogBuilder addRequestPayLoad(String requestPayLoad) {
        this.put("Q_PAYLOAD", requestPayLoad);
        return this;
    }

    public AccessJsonLogBuilder addResponsePayLoad(final String responsePayLoad,
                                                   HttpServletResponse response) {
        String contentType = response.getContentType();
        try {
            Optional.ofNullable(Strings.emptyToNull(contentType))
                .filter(c -> c.startsWith("application/json"))
                .ifPresent(c -> {

                    if (responsePayLoad.startsWith("{") && responsePayLoad.endsWith("}")) {
                        try {
                            Map<String, Object> map = this.hzJson.str2obj(responsePayLoad, Map.class);
                            String code = String.valueOf(map.get("code"));
                            if (StringUtils.isNotBlank(code)) {
                                this.put("R_CODE", code);
                            }
                        } catch (Exception e) {
                            log.warn("get result code: {}", e.getMessage());
                            this.put("R_CODE", String.valueOf(RespResultCode.SUCCESS.getCode()));
                        }
                    } else {
                        this.put("R_CODE", String.valueOf(RespResultCode.SUCCESS.getCode()));
                    }

                });
        } catch (Exception e) {
            log.error("get result code:", e);
        }
        this.put("P_PAYLOAD", responsePayLoad);
        return this;
    }

    public void log() {
        log.info(hzJson.obj2string(getData()));
    }

}
