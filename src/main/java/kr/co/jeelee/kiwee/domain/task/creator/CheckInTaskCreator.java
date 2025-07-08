package kr.co.jeelee.kiwee.domain.task.creator;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.task.dto.request.CheckInTaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.metadata.CheckInMetadata;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.domain.task.repository.TaskRepository;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckInTaskCreator implements TaskCreator<CheckInTaskCreateRequest> {

	private final TaskRepository taskRepository;

	private final ChannelService channelService;
	private final MemberService memberService;

	@Override
	public Task create(UUID channelId, UUID memberId, CheckInTaskCreateRequest request) {
		Channel channel = channelId != null
			? channelService.getById(channelId)
			: null;

		Member member = memberService.getById(memberId);
		CheckInMetadata metadata = request.metadata();

		if (taskRepository.existsByChannelAndMemberAndTaskTypeAndCreatedAtAfter(
			channel, member, supports(), LocalDate.now().atStartOfDay()
		)) {
			throw new FieldValidationException("TaskType", "이미 오늘 출석을 진행하였습니다.");
		}

		if ((metadata.mediaType() == null) ^ (metadata.mediaUrl() == null)) {
			throw new FieldValidationException("mediaUrl", "mediaType 유무와 같아야 합니다.");
		}

		if (metadata.mediaUrl() != null && metadata.mediaUrl().trim().isEmpty()) {
			throw new FieldValidationException("mediaUrl", "mediaUrl 은 비어있을 수 없습니다.");
		}

		return Task.of(channel, member, supports(), JsonUtil.toJson(metadata));
	}

	@Override
	public TaskType supports() {
		return TaskType.CHECK_IN;
	}

	@Override
	public Class<? extends TaskCreateRequest> supportsClass() {
		return CheckInTaskCreateRequest.class;
	}
}
