package kr.co.jeelee.kiwee.domain.task.dto.request;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.task.metadata.PlayMetadata;

public record PlayTaskCreateRequest(
	@Valid PlayMetadata metadata
) implements TaskCreateRequest {
}
