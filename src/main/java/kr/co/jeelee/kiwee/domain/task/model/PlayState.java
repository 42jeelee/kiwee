package kr.co.jeelee.kiwee.domain.task.model;

import kr.co.jeelee.kiwee.global.model.ActivityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayState {
	PLAY("시작", ActivityType.PLAY),
	LISTENING("듣기", ActivityType.PLAY),
	UPDATE("변경", ActivityType.UPDATE),
	END("종료", ActivityType.END),
	;
	private final String label;
	private final ActivityType activityType;
}
