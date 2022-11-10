package com.daesung.api.utils.date;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Slf4j
public class DaesungDateUtil {

	private static SimpleDateFormat standardDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat standardDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static Date getCurrentUTCDate() {
		return new Date(Instant.now().getEpochSecond() * 1000);
	}

	public static String dateToStandardDateTimeString(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardDateTimeString(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateTimeString.");
		}

		return dateToStandardDateTimeString(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardDateTimeString(Date date) {
		return dateToStandardDateTimeString(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardDateTimeString(Date date) {
		return dateToStandardDateTimeString(date, ZoneOffset.UTC);
	}

	public static String dateToStandardDateString(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardDateString(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateString.");
		}

		return dateToStandardDateString(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardDateString(Date date) {
		return dateToStandardDateString(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardDateString(Date date) {
		return dateToStandardDateString(date, ZoneOffset.UTC);
	}

	public static String dateToStandardTimeString(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardTimeString(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateTimeString.");
		}

		return dateToStandardTimeString(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardTimeString(Date date) {
		return dateToStandardTimeString(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardTimeString(Date date) {
		return dateToStandardTimeString(date, ZoneOffset.UTC);
	}

	public static String dateToStandardDateTimeWithMillisecondString(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardDateTimeWithMillisecondString(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateTimeString.");
		}

		return dateToStandardDateTimeWithMillisecondString(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardDateTimeWithMillisecondString(Date date) {
		return dateToStandardDateTimeWithMillisecondString(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardDateTimeWithMillisecondString(Date date) {
		return dateToStandardDateTimeWithMillisecondString(date, ZoneOffset.UTC);
	}

	public static String dateToStandardDateTimeStringWithoutDash(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardDateTimeStringWithoutDash(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateTimeString.");
		}

		return dateToStandardDateTimeStringWithoutDash(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardDateTimeStringWithoutDash(Date date) {
		return dateToStandardDateTimeStringWithoutDash(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardDateTimeStringWithoutDash(Date date) {
		return dateToStandardDateTimeStringWithoutDash(date, ZoneOffset.UTC);
	}

	public static String dateToStandardDateStringWithoutDash(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardDateStringWithoutDash(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateTimeString.");
		}

		return dateToStandardDateStringWithoutDash(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardDateStringWithoutDash(Date date) {
		return dateToStandardDateStringWithoutDash(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardDateStringWithoutDash(Date date) {
		return dateToStandardDateStringWithoutDash(date, ZoneOffset.UTC);
	}

	public static String dateToStandardTimeStringWithoutDash(Date date, ZoneId timeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss").withZone(timeZone);
		return formatter.format(date.toInstant());
	}

	public static String dateToStandardTimeStringWithoutDash(Date date, Long timeZoneMinuteDiff) {
		int hours;
		int minutes;

		if (timeZoneMinuteDiff != null) {
			hours = timeZoneMinuteDiff.intValue() / 60;
			minutes = timeZoneMinuteDiff.intValue() % 60;
		} else {
			hours = 0;
			minutes = 0;
			log.debug("Passed null timeZoneMinuteDiff value while calling dateToStandardDateTimeString.");
		}

		return dateToStandardTimeStringWithoutDash(date, ZoneOffset.ofHoursMinutes(hours, minutes));
	}

	public static String dateToStandardTimeStringWithoutDash(Date date) {
		return dateToStandardTimeStringWithoutDash(date, ZoneId.systemDefault());
	}

	public static String utcDateToStandardTimeStringWithoutDash(Date date) {
		return dateToStandardTimeStringWithoutDash(date, ZoneOffset.UTC);
	}

	public static Date parseDateString(String dateString) throws ParseException {
		if(dateString == null) {
			return null;
		}
		return standardDateFormat.parse(dateString);
	}

	public static Date parseDateTimeString(String dateTimeString) throws ParseException {
		if(dateTimeString == null) {
			return null;
		}
		return standardDateTimeFormat.parse(dateTimeString);
	}

	public static String formatToDateTimeString(Date date) {
		return standardDateTimeFormat.format(date);
	}

	public static synchronized Date getStartOfCurrentLocalDate() {
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		return localDateTimeToDate(standardLocalDateTime);
	}

	public static synchronized String getStartOfCurrentLocalDateString() {
		return standardDateTimeFormat.format(getStartOfCurrentLocalDate());
	}

	public static synchronized Date getStartOfCurrentUTCDate() {
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.UTC).atStartOfDay();
		return utcDateTimeToDate(standardLocalDateTime);
	}

	public static synchronized String getStartOfCurrentUTCDateString() {
		return standardDateTimeFormat.format(getStartOfCurrentUTCDate());
	}

	public static synchronized Date getStandard1DaysAgoDate() {
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		Date standardDate = localDateTimeToDate(standardLocalDateTime);
		return workingDateBefore(standardDate, 1);
	}

	public static synchronized String getStandard1DaysAgoDateString() {
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		Date standardDate = localDateTimeToDate(standardLocalDateTime);
		return workingDateStringBefore(standardDate, 1);
	}

	public static synchronized String getStandard3DaysAgoDateString() {
		// KST 09:00 is standard of mail send, and it's 00:00 in UTC
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		Date standardDate = localDateTimeToDate(standardLocalDateTime);
		return workingDateStringBefore(standardDate, 3);
	}

	public static Date getStandard3DaysAgoDate() {
		// KST 09:00 is standard of mail send, and it's 00:00 in UTC
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		Date standardDate = localDateTimeToDate(standardLocalDateTime);
		return workingDateBefore(standardDate, 3);
	}

	public static Date getStandard7DaysAgoDate() {
		// KST 09:00 is standard of mail send, and it's 00:00 in UTC
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		Date standardDate = localDateTimeToDate(standardLocalDateTime);
		return workingDateBefore(standardDate, 7);
	}

	public static synchronized String getStandard7DaysAgoDateString() {
		// KST 09:00 is standard of mail send, and it's 00:00 in UTC
		LocalDateTime standardLocalDateTime = LocalDate.now(ZoneOffset.systemDefault()).atStartOfDay();
		Date standardDate = localDateTimeToDate(standardLocalDateTime);
		return workingDateStringBefore(standardDate, 7);
	}

	public static synchronized String workingDateStringBefore(String dateString, int dayBefore) throws ParseException {
		Date currentDate = standardDateTimeFormat.parse(dateString);
		return workingDateStringBefore(currentDate, dayBefore);
	}

	public static synchronized String workingDateStringBefore(Date date, int dayBefore) {
		return standardDateTimeFormat.format(workingDateBefore(date, dayBefore));
	}

	public static synchronized Date workingDateBefore(Date date, int dayBefore) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDateTime startOfUtcDay = localDateTime.toLocalDate().atStartOfDay();
		LocalDateTime beforeLocalDateTime = getMinusWorkingDay(startOfUtcDay, dayBefore);
		return localDateTimeToDate(beforeLocalDateTime);
	}

	public static synchronized Date workingDateBefore(String dateString, int dayBefore) throws ParseException {
		Date currentDate = standardDateTimeFormat.parse(dateString);
		return workingDateBefore(currentDate, dayBefore);
	}

	private static LocalDateTime dateToLocalDateTime(Date date) {
		return date.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}

	private static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	private static Date utcDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
	}

	private static LocalDateTime getMinusWorkingDay(LocalDateTime localDateTime, int dayBefore) {
		int divided = dayBefore / 5;
		int remaining = dayBefore % 5;
		int dayOfWeek = localDateTime.getDayOfWeek().getValue();

		int totalMinusDay = divided * 7 + remaining;
		if (dayOfWeek <= remaining) {
			totalMinusDay = totalMinusDay + 2;
		}

		return localDateTime.minus(Period.ofDays(totalMinusDay));
	}

	private DaesungDateUtil() {
	}
}
