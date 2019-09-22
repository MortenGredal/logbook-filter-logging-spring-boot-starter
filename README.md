# logbook-filter-logging-spring-boot-starter
Logging module that utilizes both servlet filters and zalando's logbook module



#### Purpose
This starter is meant for adding additional info to logstash formatted json in separate fields making it easier to search across logging entries.  

Zalandos logbook was chosen because it has field masking out of the box.    


Logging output looks like this:
```
{
    "LOG_DATEFORMAT_PATTERN": "yyyy-MM-dd HH:mm:ss.SSS",
    "LOG_LEVEL_PATTERN": "%5p [sampleapp,%X{X-B3-TraceId:-},%X{X-B3-SpanId:-},%X{X-Span-Export:-}]",
    "app_name": "sampleapp",
    "ip_address": "192.168.87.104",
    "level": "TRACE",
    "logger_name": "o.z.logbook.Logbook",
    "traceId": "a75a8c9dcba2e340",
    "spanId": "a75a8c9dcba2e340",
    "spanExportable": "false",
    "X-Span-Export": "false",
    "http_method": "GET",
    "X-B3-SpanId": "a75a8c9dcba2e340",
    "X-B3-TraceId": "a75a8c9dcba2e340",
    "client_ip": "0:0:0:0:0:0:0:1",
    "request_url": "http://localhost:8080/actuator/health",
    "request_uri": "/actuator/health",
    "message": "{\"request_body\":null}",
    "thread_name": "http-nio-8080-exec-1",
    "timestamp": "2019-09-22T13:16:01.657Z"
}{
    "LOG_DATEFORMAT_PATTERN": "yyyy-MM-dd HH:mm:ss.SSS",
    "LOG_LEVEL_PATTERN": "%5p [sampleapp,%X{X-B3-TraceId:-},%X{X-B3-SpanId:-},%X{X-Span-Export:-}]",
    "app_name": "sampleapp",
    "ip_address": "192.168.87.104",
    "level": "TRACE",
    "logger_name": "o.z.logbook.Logbook",
    "traceId": "a75a8c9dcba2e340",
    "spanExportable": "false",
    "X-Span-Export": "false",
    "request_url": "http://localhost:8080/actuator/health",
    "request_uri": "/actuator/health",
    "spanId": "a75a8c9dcba2e340",
    "http_method": "GET",
    "X-B3-SpanId": "a75a8c9dcba2e340",
    "X-B3-TraceId": "a75a8c9dcba2e340",
    "client_ip": "0:0:0:0:0:0:0:1",
    "http_status": "200",
    "response_time": "52",
    "message": "{\"response_body\":{\"status\":\"UP\"}}",
    "thread_name": "http-nio-8080-exec-1",
    "timestamp": "2019-09-22T13:16:01.711Z"
}
```
#### Modules used
**spring-cloud-starter-zipkin** - Appends fields related to traceId and spanId  
**logbook-spring-boot-starter** - Handles the logging of request and response bodies  

#### Current issues  
~~The LoggingAutoConfiguration is unable to get the ${spring.application.name} and ${spring.cloud.client.ip-address}~~  
  
#### Thanks to
[Jhipster](https://www.jhipster.tech/) for providing the setup of switching between json and non-json by changing a property.  

[zalando / logbook](https://github.com/zalando/logbook) for the logbook project