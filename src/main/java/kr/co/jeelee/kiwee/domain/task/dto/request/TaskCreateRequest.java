package kr.co.jeelee.kiwee.domain.task.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "taskType", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(value = CheckInTaskCreateRequest.class, name = "CHECK_IN"),
	@JsonSubTypes.Type(value = ReviewTaskCreateRequest.class, name = "REVIEW")
})
public sealed interface TaskCreateRequest permits CheckInTaskCreateRequest, ReviewTaskCreateRequest {
}
