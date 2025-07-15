package kr.co.jeelee.kiwee.domain.task.creator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.task.dto.request.RecordTaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.metadata.RecordMetadata;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecordTaskCreator implements TaskCreator<RecordTaskCreateRequest> {

	private final MemberService memberService;

	@Override
	public Task create(UUID memberId, RecordTaskCreateRequest request) {
		Member member = memberService.getById(memberId);
		RecordMetadata metadata = request.metadata();

		if ((metadata.mediaType() == null) ^ (metadata.mediaUrl() == null)) {
			throw new FieldValidationException("mediaUrl", "mediaType 유무와 같아야 합니다.");
		}

		if (metadata.mediaUrl() != null && metadata.mediaUrl().trim().isEmpty()) {
			throw new FieldValidationException("mediaUrl", "mediaUrl 은 비어있을 수 없습니다.");
		}

		return Task.of(member, supports(), ActivityType.RECORD, JsonUtil.toJson(metadata));
	}

	@Override
	public TaskType supports() {
		return TaskType.RECORD;
	}

	@Override
	public Class<? extends TaskCreateRequest> supportsClass() {
		return RecordTaskCreateRequest.class;
	}
}
