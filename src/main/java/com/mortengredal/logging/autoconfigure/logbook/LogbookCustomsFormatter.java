package com.mortengredal.logging.autoconfigure.logbook;

import com.mortengredal.logging.autoconfigure.util.MDCUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zalando.logbook.*;

import java.io.IOException;
import java.util.Map;

public class LogbookCustomsFormatter implements HttpLogFormatter {

     private final JsonHttpLogFormatter delegate;

     public LogbookCustomsFormatter(final ObjectMapper mapper) {
         this.delegate = new JsonHttpLogFormatter(mapper);
     }

     public String format(final Precorrelation<HttpRequest> precorrelation) throws IOException {
         Map<String, Object> request = delegate.prepare(precorrelation);

         Object body = request.get("body");
         Map<String, Object> content = new java.util.HashMap<>();
         content.put("request_body", body);
         return delegate.format(content);
     }

     public String format(final Correlation<HttpRequest, HttpResponse> correlation) throws IOException {
         Map<String, Object> response = delegate.prepare(correlation);
         MDCUtil.addHttpStatusIfNeeded((int) response.get("status"));
         MDCUtil.addResponseTimeToMDC((long) response.get("duration"));

         Object body = response.get("body");
         Map<String, Object> content = new java.util.HashMap<>();
         content.put("response_body", body);
         return delegate.format(content);
      }

 }