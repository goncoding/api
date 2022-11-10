package com.daesung.api.utils.image;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j2
public class AccessLogUtil {

	private static final DateTimeFormatter formatter =
			DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS").withZone(ZoneOffset.UTC);

	public static void pageAccessLog(String url, String queryString) {
		String dateTime = getUTCCurrentTime();
		Optional<String> queryStringOptional = Optional.ofNullable(queryString);

		log.log(Level.INFO, "[{}] Accessed URL: {}", dateTime, url);
		queryStringOptional.ifPresent(queryStringValue -> log.log(Level.INFO, "[{}] Query string: {}", dateTime, queryStringValue));
	}

	public static void pageAccessLog(HttpServletRequest request) {
		pageAccessLog(request.getRequestURL().toString(), request.getQueryString());
	}

	public static void pageErrorLog(int statusCode, String url, String queryString) {
		String dateTime = getUTCCurrentTime();
		Optional<String> queryStringOptional = Optional.ofNullable(queryString);

		log.log(Level.INFO, "[{}] {} Error accessed URL: {}", dateTime, statusCode, url);
		queryStringOptional.ifPresent(queryStringValue -> log.log(Level.INFO, "[{}] Query string: {}", dateTime, queryStringValue));
	}

	public static void fileViewAccessLog(HttpServletRequest request) {
		String dateTime = getUTCCurrentTime();
		String url = request.getRequestURL().toString();

		log.log(Level.DEBUG, "[{}] View file URL: {}", dateTime, url);
	}

	private static String getUTCCurrentTime() {
		Instant requestTime = Instant.now();
		return formatter.format(requestTime);
	}

	private AccessLogUtil() {
	}
}
