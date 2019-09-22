package com.mortengredal.logging.autoconfigure.filter;

import com.mortengredal.logging.autoconfigure.util.MDCUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import java.io.IOException;


@Order(Ordered.HIGHEST_PRECEDENCE) //We want to get as accurate
public class RequestTimingFilter  implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long responseTime = System.currentTimeMillis() - start;
            MDCUtil.addResponseTimeToMDC(responseTime);
        }
    }
}
