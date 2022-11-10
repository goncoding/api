package com.daesung.api.utils;

import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HtmlStringUtil {

	public static String convertNewLineToBr(String htmlString) {
		if (htmlString == null) {
			return null;
		}

		return htmlString.replace("\r\n", "<br>")
				.replace("([\r\n])", "<br>");
	}

	@SneakyThrows
	public static String encodeUrlAsUtf8(String string) {
		if(string == null) {
			return null;
		}
		return URLEncoder.encode(string, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
	}

	private HtmlStringUtil() {
	}
}
