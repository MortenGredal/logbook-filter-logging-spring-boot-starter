package com.mortengredal.logging.autoconfigure.util;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MDCUtil {

    /**
     * Copy basic request info to MDC so it's accessible from the beginning of the request
     * This works both on all request results, 2xx, 5xx etc.
     * @param request The inbound request
     */
    public static void addRequestInfoToMDC(HttpServletRequest request) {
        MDC.put("query_string", request.getQueryString());
        MDC.put("request_uri", request.getRequestURI());
        MDC.put("http_method", request.getMethod());
        MDC.put("remote_user", request.getRemoteUser());

        String requestUrl = getRequestUrl(request);

        MDC.put("request_url", requestUrl);

        String client_ip = request.getRemoteAddr();
        if (StringUtils.hasLength(client_ip)) {
            MDC.put("client_ip", client_ip);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            MDC.put("session", session.getId());
        }
        String client_user = request.getRemoteUser();
        if (client_user != null) {
            MDC.put("client_user", client_user);
        }
    }
    private static String getRequestUrl(HttpServletRequest request) {
        return String.format("%s://%s:%d%s%s%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath(),
                request.getRequestURI(),
                StringUtils.hasLength(request.getQueryString()) ? "?" + request.getQueryString() : "");
    }

    public static void addResponseTimeToMDC(long responseTime) {
        MDC.put("response_time", responseTime + "");
    }

    /**
     * Adds the http_status if the request is successful
     */
    public static void addHttpStatusIfNeeded(int status) {
        String http_status = MDC.get("http_status");
        if (http_status == null){
            MDC.put("http_status", status + "");
        }
    }
}
