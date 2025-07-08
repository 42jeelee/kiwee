package kr.co.jeelee.kiwee.domain.task.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.util.JsonUtil;

public record TaskResponse(
	UUID id, ChannelSimpleResponse channel, MemberSimpleResponse member,
	TaskType taskType, Object metadata,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static TaskResponse from(Task task) {
		return new TaskResponse(
			task.getId(),
			ChannelSimpleResponse.from(task.getChannel()),
			MemberSimpleResponse.from(task.getMember()),
			task.getTaskType(),
			JsonUtil.fromJson(task.getMetadata(), task.getTaskType().getMetadataClass()),
			task.getUpdatedAt(),
			task.getCreatedAt()
		);
	}
}
