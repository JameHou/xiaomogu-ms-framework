package com.github.xiaomogu.web.log;

import com.github.xiaomogu.commons.jackson.HzJson;
import com.github.xiaomogu.web.config.SpringMvcProperties;
import com.github.xiaomogu.web.utils.WebLogUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class AccessLogFilter extends OncePerRequestFilter {
    @NonNull
    @Getter
    private SpringMvcProperties springMvcProperties;
    @NonNull
    @Getter
    private HzJson hzJson;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //log.info("request AccessLogFilter...........");
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        if(WebLogUtils.isNormalRequest(request)){
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(requestWrapper,responseWrapper);
            String requestPayload = WebLogUtils.getPayLoad(requestWrapper.getContentAsByteArray(),
                    request.getCharacterEncoding());
            String responsePayload = WebLogUtils.getPayLoad(responseWrapper.getContentAsByteArray(),
                    response.getCharacterEncoding());
            responseWrapper.copyBodyToResponse();

            AccessJsonLogBuilder
                .accessJsonLogBuilder(hzJson, springMvcProperties)
                .addRequestPayLoad(requestPayload)
                .put(request)
                .addResponsePayLoad(responsePayload,response)
                .put(response)
                .put("_COST_",stopwatch.getTime())
                .log();

        }else {
            filterChain.doFilter(request,response);
        }
        //log.info("response AccessLogFilter ..............");
    }

}
