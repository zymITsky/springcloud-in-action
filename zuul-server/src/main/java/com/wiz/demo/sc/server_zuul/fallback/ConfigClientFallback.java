
package com.wiz.demo.sc.server_zuul.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ConfigClientFallback implements FallbackProvider {

	private final Logger logger = LoggerFactory.getLogger(FallbackProvider.class);

	@Override
	public String getRoute() {
		return "config-client";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		logger.info("===> config-client Hystrix - Fallback : {}", route);
		if (cause != null && cause.getCause() != null) {
			String reason = cause.getCause().getMessage();
			logger.info("===> Excption {}", reason);
		}
		return new ClientHttpResponse() {
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.OK;
			}
			@Override
			public int getRawStatusCode() throws IOException {
				return 200;
			}
			@Override
			public String getStatusText() throws IOException {
				return "OK";
			}
			@Override
			public void close() {

			}
			@Override
			public InputStream getBody() throws IOException {
				return new ByteArrayInputStream("The service [config-client] unavailable. Please contact with IT.".getBytes());
			}
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}
		};
	}
}