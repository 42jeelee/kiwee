package kr.co.jeelee.kiwee.domain.reputations.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ReputationsCreateRequest(
	@NotNull(message = "giverId can't be Null.") UUID giverId,
	@NotNull(message = "receiverId can't be Null.") UUID receiverId,
	Boolean isUp
) {
}
