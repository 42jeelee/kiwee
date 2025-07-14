package kr.co.jeelee.kiwee.global.vo;

import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;

@Embeddable
public record TimeRange(
	LocalTime startTime, LocalTime endTime
) {
	public TimeRange {
		if (startTime.isAfter(endTime)) {
			throw new InvalidParameterException("endTime", "startTime 보다 이른 시간일 수 없습니다.");
		}
	}

	public boolean isWithin(LocalTime time) {
		return !time.isBefore(startTime) && !time.isAfter(endTime);
	}
}
