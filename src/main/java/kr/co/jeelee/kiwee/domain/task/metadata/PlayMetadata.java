package kr.co.jeelee.kiwee.domain.task.metadata;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.task.model.PlayState;

public record PlayMetadata(
	@NotNull(message = "playState can't be Null.") PlayState playState,
	String applicationId,
	String name,
	List<String> details
) {
}
