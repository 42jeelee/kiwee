package kr.co.jeelee.kiwee.domain.task.dto.request;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.task.metadata.ReviewMetadata;

public record ReviewTaskCreateRequest(
	@Valid ReviewMetadata metadata
) implements TaskCreateRequest {
}
