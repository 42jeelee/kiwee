package kr.co.jeelee.kiwee.global.vo;

import java.io.Serializable;
import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class TimeRange implements Serializable {

	private LocalTime startTime;
	private LocalTime endTime;

	public TimeRange(LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidParameterException("endTime", "startTime 보다 이른 시간일 수 없습니다.");
		}
	}

	public boolean isWithin(LocalTime time) {
		return !time.isBefore(startTime) && !time.isAfter(endTime);
	}
}
