package com.mortengredal.logging.autoconfigure.filter;

import com.mortengredal.logging.autoconfigure.util.MDCUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class StatusCodeFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            MDCUtil.addHttpStatusIfNeeded(((HttpServletResponse) response).getStatus());
        }
    }
}