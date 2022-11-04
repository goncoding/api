package com.daesung.api.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

	/**
	 * Return true if src is null or isEmpty or equal to 'null' regardless of case.
	 *
	 * @param src(String)
	 * @return boolean
	 */
	public static final boolean isEmpty(Object src) {
		return src == null ||
				src.toString().trim().isEmpty() ||
				"null".equalsIgnoreCase(src.toString().trim());
	}

	/**
	 * Return 'src' string if it matters, or 'replace' string.
	 *
	 * @param src(Source String)
	 * @param replace(replacement String)
	 * @return either one of two Strings
	 * @refer StrUtil.isEmpty()
	 */
	public static final String nvl(Object src, String replace) {
		return isEmpty(src) ? replace : src.toString();
	}

	/**
	 * Return a String which doesn't include SQL characters.
	 *
	 * @param src(String)
	 * @return String
	 * @target ;;+, ', ", union, <, >, --, (, ), =, +
	 */
	public static final String SQLInjection(String src) {
		return src.replaceAll(";;+", ";")
				.replaceAll("(\'|'|%27)", "&#39;")
				.replaceAll("(\"|%22)", "&#34;")
				.replaceAll("(?i)union", "")
				.replaceAll("(<|%3C|&#60;)", "&lt;")
				.replaceAll("(>|%3E|&#62;)", "&gt;")
				.replace("--", "&#45;&#45;")
				.replace("(", "&#40;")
				.replace(")", "&#41;")
				.replace("=", "&#61;")
				.replace("+", "&#43;");
	}

	/**
	 * Return a String which doesn't include words related to XSS
	 *
	 * @param src(String)
	 * @return String
	 * @target %73%63%72%69%70%74, alert, append, applet, bgsound, binding, blink, charset, cookie, create, document, embed, eval, expression, frame, frameset, iframe, ilayer, innerHTML, javascript, layer, link, msgbox, object, onabort, onactivate, onafterprint, onafterupdate, onbefore, onbeforeactivate, onbeforecopy, onbeforecut, onbeforedeactivate, onbeforeeditfocus, onbeforepaste, onbeforeprint, onbeforeunload, onbeforeupdate, onblur, onbounce, oncellchange, onchange, onclick, oncontextmenu, oncontrolselect, oncopy, oncut, ondataavailable, ondatasetchanged, ondatasetcomplete, ondblclick, ondeactivate, ondrag, ondragend, ondragenter, ondragleave, ondragover, ondragstart, ondrop, onerror, onerrorupdate, onfilterchange, onfinish, onfocus, onfocusin, onfocusout, onhelp, onkeydown, onkeypress, onkeyup, onlayoutcomplete, onload, onlosecapture, onmousedown, onmouseenter, onmouseleave, onmousemove, onmouseout, onmouseover, onmouseup, onmousewheel, onmove, onmoveend, onmovestart, onpaste, onpropertychange, onreadystatechange, onreset, onresize, onresizeend, onresizestart, onrowenter, onrowexit, onrowsdelete, onrowsinserted, onscroll, onselect, onselectionchange, onselectstart, onstart, onstop, onsubmit, onunload, refresh, script, string, unescape, vbscript, void
	 */
	public static final String xss(Object src) {
		String _src = "";
		if (isEmpty(src))
			_src = "";
		else {
			String blackTagList = "%73%63%72%69%70%74|alert|append|applet|bgsound|binding|blink|charset|cookie|create|document|embed|eval|expression|frame|frameset|iframe|ilayer|innerHTML|javascript|layer|link|msgbox|object|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditfocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror|onerrorupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmouseout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizeend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload|refresh|script|string|unescape|vbscript|void";
			_src = _src.replaceAll("(?i)(" + blackTagList + ")", "");
		}
		return _src;
	}

	/**
	 * Replace a String with int. If Exception occurs during replacement, return 'defaultValue'.
	 *
	 * @param num(String)
	 * @param defaultValue(replacement int)
	 * @return either replaced int or defaultValue int
	 */
	public static final int parseInt(String num, int defaultValue) {
		int result = defaultValue;
		try {
			result = Integer.parseInt(num);
		} catch (Exception e) {
			System.out.println(String.format("Parsing %s into Integer Failed.", num));
		}
		return result;
	}

	/**
	 * Replace a String with long. If Exception occurs during replacement, return 'defaultValue'.
	 *
	 * @param num(String)
	 * @param defaultValue(replacement long)
	 * @return either replaced long or defaultValue long
	 */
	public static final Long parseLong(String num, Long defaultValue) {
		Long result = defaultValue;
		try {
			result = Long.parseLong(num);
		} catch (Exception e) {
			System.out.println(String.format("Parsing %s into Long Failed.", num));
		}
		return result;
	}

	/**
	 * Return a 'UTF-8'-encoded String
	 *
	 * @param str(String)
	 * @return 'UTF-8'-encoded String
	 */
	public static final String encode(String str) {
		str = nvl(str, "");
		try {
			str = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}

	/**
	 * Return a String which is encoded only Korean.
	 *
	 * @param src
	 * @return String
	 */
	public static final String encodeKR(String src) {
		Pattern p = Pattern.compile("[ㄱ-힣]");                                    // 정규식을 사용하여, 한글로 된 문자만을 대상으로
		Matcher m = p.matcher(src);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			try {
				m.appendReplacement(sb, URLEncoder.encode(m.group(), "utf-8"));    // 한글만 UTF-8로 인코딩
			} catch (Exception e) {
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Return a 'UTF-8'-decoded String
	 *
	 * @param str(String)
	 * @return 'UTF-8'-decoded String
	 */
	public static final String decode(String str) {
		str = nvl(str, "");
		try {
			str = URLDecoder.decode(URLDecoder.decode(str, "UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}

	/**
	 * Return a random UUID with extension from given 'fileName'
	 *
	 * @param fileName(String)
	 * @return random UUID with extension
	 */
	public static final String getUniqueFileName(String fileName) {
		int idx = -1;
		int tmp = -1;
		while ((tmp = fileName.indexOf(".", tmp + 1)) != -1) {
			idx = tmp;
		}
		String fName = UUID.randomUUID().toString();
		if (idx > 0) {
			fName += fileName.substring(idx);
		}
		return fName;
	}

	/**
	 * Return a masked String if it's email or phone number
	 *
	 * @param src(String)
	 * @return String
	 */
	public static final String masking(Object src) {
		src = nvl(src, "");

		Pattern pat = Pattern.compile("(.{3})(.*)@(.+)\\.(.+)");    // 이메일 표현 정규식
		Matcher mat = pat.matcher(src.toString());
		if (mat.find()) {                                            // 이메일로 판단되면
			String gr2 = mat.group(2).replaceAll(".", "*");        // 이메일 아이디 부분을 맨 팝 3글자를 빼고 모두 * 로 변환
			return String.format("%s%s@%s.%s", mat.group(1), gr2, mat.group(3), mat.group(4));
		}

		pat = Pattern.compile("^\\d+$");                            // 연락처 표현 정규식
		mat = pat.matcher(src.toString());
		if (mat.find()) {                                            // 연락처 번호로 판단되면
			String[][] tel_pat = {
					{"^(01\\d)\\d{4}(\\d{4})$", "$1-****-$2"},    // 11자리 핸드폰
					{"^(01\\d)\\d{3}(\\d{4})$", "$1-***-$2"},        // 10자리 핸드폰
					{"^(07\\d)\\d{4}(\\d{4})$", "$1-****-$2"},    // 070 등 인터넷전화
					{"^(\\d{2})\\d{4}(\\d{4})$", "$1-****-$2"},    // 10자리 서비스전화
					{"^(\\d{2})\\d{3}(\\d{4})$", "$1-***-$2"},    // 지역번호 2자리 집전화
					{"^(\\d{3})\\d{3}(\\d{4})$", "$1-***-$2"},    // 지역번호 4자리 집전화
					{"^(\\d{3})\\d{4}(\\d{4})$", "$1-****-$2"}};    // 기타
			for (String[] arr : tel_pat) {
				src = src.toString().replaceAll(arr[0], arr[1]);                // 각 연락처 형식에 따라 마스킹 처리
			}
			return src.toString();
		}


		pat = Pattern.compile("(.)([^\\s]+)");                    // 이메일, 연락처 외에 나머지 전체의 정규표현식. 띄어쓰기를 기준으로 단어별로 맨 앞글자를 제외하고 모두 마스킹.
		mat = pat.matcher(src.toString());
		while (mat.find()) {
			src = src.toString().replace(mat.group(0), mat.group(1) + mat.group(2).replaceAll(".", "*"));
		}
		return src.toString();
	}

	/**
	 * Return a String which is joined all elements in 'arr' with 'deli'
	 *
	 * @param arr(String Array)
	 * @param deli(String delimeter)
	 * @return String
	 */
	public static final String join(Object[] arr, String deli) {
		String result = "";
		for (Object s : arr) {
			result += s.toString() + deli;
		}
		return result.replaceAll(deli + "+", deli).replaceAll("(^" + deli + "|" + deli + "$|)", "");
	}


	/**
	 * 국문 영문 중문 리턴해줌
	 *
	 * @return String
	 */
	public static final String getLanguage(String lang) {
		String language = null;
		if (lang.equals("ko")) {
			language = "국문";
		} else if (lang.equals("en")) {
			language = "영문";
		} else if (lang.equals("cn") || lang.equals("zh")) {
			language = "중문";
		}
		return language;
	}

	/**
	 * @return String
	 */
	public static final String removeHtmlString(String originalString) {
		if (originalString != null) {
			return originalString.replaceAll("&rsquo;", "’")
					.replaceAll("&lsquo;", "`")
					.replaceAll("&quot;", "\"")
					.replaceAll("&#39;", "'")
					.replaceAll("&#33;", "!")
					.replaceAll("&#63;", "?")
					.replaceAll("&#64;", "@")
					.replaceAll("&#58;", ":")
					.replaceAll("&#126;", "~")
					.replaceAll("&amp;", "&")
					.replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">");
		} else {
			return null;
		}
	}

	public static String replaceNewLineToHtmlBr(String htmlString) {
		if (htmlString != null) {
			return htmlString.replaceAll("\\r", "").replaceAll("\\n", "<br/>");
		} else {
			System.out.println("Warning on replace string: parameter is null.");
			return "";
		}
	}
	
	public static String replaceBlankNewline(String temp) {
		if(temp != null && temp != "") {
			return temp.replaceAll("(\\s|\r\n|\r|\n|\n\r)", "");
		} else {
			return "";
		}
	}
}
