package kr.co.jeelee.kiwee.domain.task.dto.request;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.task.metadata.RecordMetadata;

public record RecordTaskCreateRequest(
	@Valid RecordMetadata metadata
) implements TaskCreateRequest {
}
