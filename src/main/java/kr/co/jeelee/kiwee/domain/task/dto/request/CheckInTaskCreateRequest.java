package kr.co.jeelee.kiwee.domain.task.dto.request;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.task.metadata.CheckInMetadata;

public record CheckInTaskCreateRequest(
	@Valid CheckInMetadata metadata
) implements TaskCreateRequest {
}
