package kr.co.jeelee.kiwee.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.List;

import com.nimbusds.jose.util.Pair;

import kr.co.jeelee.kiwee.global.exception.common.UnsupportedTermTypeException;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;

public class TermUtil {

	public static Pair<LocalDateTime, LocalDateTime> getTermPeriod(TermType termType, LocalDate date) {
		return switch (termType) {
			case DAILY -> {
				LocalDateTime start = date.atStartOfDay();
				LocalDateTime end = date.plusDays(1).atStartOfDay();
				yield  Pair.of(start, end);
			}
			case WEEKLY -> {
				LocalDateTime start = date.with(DayOfWeek.MONDAY).atStartOfDay();
				LocalDateTime end = start.plusWeeks(1);
				yield  Pair.of(start, end);
			}
			case MONTHLY -> {
				LocalDateTime start = date.withDayOfMonth(1).atStartOfDay();
				LocalDateTime end = start.plusMonths(1);
				yield  Pair.of(start, end);
			}
			case YEARLY -> {
				LocalDateTime start = date.withDayOfYear(1).atStartOfDay();
				LocalDateTime end = start.plusYears(1);
				yield  Pair.of(start, end);
			}
			default -> throw new UnsupportedTermTypeException();
		};
	}

	public static Pair<LocalDateTime, LocalDateTime> getTermPeriod(TermType termType, int prev) {
		LocalDate today = LocalDate.now();

		return switch (termType) {
			case DAILY -> getTermPeriod(termType, today.minusDays(prev));
			case WEEKLY -> getTermPeriod(termType, today.minusWeeks(prev));
			case MONTHLY -> getTermPeriod(termType, today.minusMonths(prev));
			case YEARLY -> getTermPeriod(termType, today.minusYears(prev));
			default -> throw new UnsupportedTermTypeException();
		};
	}

	public static LocalDate getStartTerm(TermType termType, LocalDate date) {
		return switch (termType) {
			case DAILY -> date;
			case WEEKLY -> date.with(DayOfWeek.MONDAY);
			case MONTHLY -> date.withDayOfMonth(1);
			case YEARLY -> date.withDayOfYear(1);
			default -> throw new UnsupportedTermTypeException();
		};
	}

	public static LocalDate getStartTerm(TermType termType, int next) {
		LocalDate today = LocalDate.now();

		return switch (termType) {
			case DAILY -> getStartTerm(termType, today.plusDays(next));
			case WEEKLY -> getStartTerm(termType, today.plusWeeks(next));
			case MONTHLY -> getStartTerm(termType, today.plusMonths(next));
			case YEARLY -> getStartTerm(termType, today.plusYears(next));
			default -> throw new UnsupportedTermTypeException();
		};
	}

	public static boolean isTodayMatched(RepeatCondition condition, LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		int dayOfMonth = date.getDayOfMonth();
		MonthDay monthDay = MonthDay.from(date);

		boolean matchesDayOfWeek = condition.days() != null && condition.days().contains(dayOfWeek);
		boolean matchesDayOfMonth = condition.monthDays() != null && condition.monthDays().contains(dayOfMonth);
		boolean matchesMonthDay = condition.yearlyDates() != null && condition.yearlyDates().contains(monthDay);

		return matchesDayOfWeek || matchesDayOfMonth || matchesMonthDay;
	}

	public static List<LocalDate> getDateByDayOfWeek(List<DayOfWeek> dayOfWeeks, int prev) {
		LocalDate today = LocalDate.now().minusWeeks(prev);

		LocalDate weekStart = today.with(DayOfWeek.MONDAY);

		return dayOfWeeks.stream()
			.map(weekStart::with)
			.toList();
	}

}
