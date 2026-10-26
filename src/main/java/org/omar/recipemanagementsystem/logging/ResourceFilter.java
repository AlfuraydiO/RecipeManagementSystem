package org.omar.recipemanagementsystem.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResourceFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(ResourceFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (shouldNotFilter(request)) {
      filterChain.doFilter(request, response);
    }

    MDC.put("request-id", UUID.randomUUID().toString());
    var creq = new ContentCachingRequestWrapper(request,0);
    var cres = new ContentCachingResponseWrapper(response);
    long start = System.currentTimeMillis();
    filterChain.doFilter(creq, cres);
    long duration = System.currentTimeMillis() - start;
    logRequest(creq);
    logResponse(cres, duration);
    cres.copyBodyToResponse();
    MDC.clear();


  }

  private String getBody(byte[] content, String encoding) {
    if (content == null || content.length == 0) return "";
    try {
      return new String(content, encoding != null ? encoding : StandardCharsets.UTF_8.name()).replaceAll("\\R+","");
    } catch (Exception e) {
      log.error("unable to decode string");
      return "";
    }
  }


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/actuator");
  }

  private void logRequest(ContentCachingRequestWrapper request) {
    String body = getBody(request.getContentAsByteArray(), request.getCharacterEncoding());

    log.info("{} {} {} Remote IP:{} Body: {}",
            request.getMethod(),
            request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""),
            request.getProtocol(),
            request.getRemoteAddr(),
            body.isBlank() ? "[empty]" : body);
  }

  private void logResponse(ContentCachingResponseWrapper response, long duration) {
    String body = getBody(response.getContentAsByteArray(), response.getCharacterEncoding());

    log.info("Status:{} Duratio:{}ms Body:{}",
            response.getStatus(),
            duration,
            body.isBlank() ? "[empty]" : body);
  }

}

