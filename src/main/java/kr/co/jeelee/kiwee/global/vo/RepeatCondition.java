package kr.co.jeelee.kiwee.global.vo;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.List;

public record RepeatCondition(
	TimeRange timeRange,
	List<DayOfWeek> days,
	List<Integer> monthDays,
	List<MonthDay> yearlyDates
) {
}
