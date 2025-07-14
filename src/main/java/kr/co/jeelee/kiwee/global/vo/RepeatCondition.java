package kr.co.jeelee.kiwee.global.vo;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.List;
import java.util.Optional;

public record RepeatCondition(
	TimeRange timeRange,
	List<DayOfWeek> days,
	List<Integer> monthDays,
	List<MonthDay> yearlyDates
) {
	public RepeatCondition mergeWith(RepeatCondition other) {
		if (other == null) return this;

		return new RepeatCondition(
			Optional.ofNullable(other.timeRange).orElse(this.timeRange),
			Optional.ofNullable(other.days).orElse(this.days),
			Optional.ofNullable(other.monthDays).orElse(this.monthDays),
			Optional.ofNullable(other.yearlyDates).orElse(this.yearlyDates)
		);
	}
}
