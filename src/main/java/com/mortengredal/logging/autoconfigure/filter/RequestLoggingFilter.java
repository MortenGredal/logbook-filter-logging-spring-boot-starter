package com.mortengredal.logging.autoconfigure.filter;

import com.mortengredal.logging.autoconfigure.util.MDCUtil;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This adds fields to the MDC fetched from the inbound request such as
 * request_uri, http_method, request_parameters etc.
 */

@Order(2)
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        //Log request info before it's being processed
        MDCUtil.addRequestInfoToMDC((HttpServletRequest) request);
        chain.doFilter(request, response);
    }
}
