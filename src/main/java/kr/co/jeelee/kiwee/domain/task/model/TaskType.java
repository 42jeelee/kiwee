package kr.co.jeelee.kiwee.domain.task.model;

import kr.co.jeelee.kiwee.domain.task.metadata.CheckInMetadata;
import kr.co.jeelee.kiwee.domain.task.metadata.ReviewMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskType {
	CHECK_IN("출석", CheckInMetadata.class),
	REVIEW("리뷰", ReviewMetadata.class),
	;
	private final String label;
	private final Class<?> metadataClass;
}
