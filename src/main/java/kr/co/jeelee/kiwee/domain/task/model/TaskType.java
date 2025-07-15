package kr.co.jeelee.kiwee.domain.task.model;

import kr.co.jeelee.kiwee.domain.task.metadata.RecordMetadata;
import kr.co.jeelee.kiwee.domain.task.metadata.PlayMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskType {
	RECORD("기록", RecordMetadata.class),
	PLAY("활동", PlayMetadata.class),
	;
	private final String label;
	private final Class<?> metadataClass;
}
