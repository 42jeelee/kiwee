package kr.co.jeelee.kiwee.domain.task.creator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.task.dto.request.PlayTaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.metadata.PlayMetadata;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlayTaskCreator implements TaskCreator<PlayTaskCreateRequest> {

	private final MemberService memberService;

	@Override
	public Task create(UUID memberId, PlayTaskCreateRequest request) {
		Member member = memberService.getById(memberId);
		PlayMetadata metadata = request.metadata();

		return Task.of(member, supports(), metadata.playState().getActivityType(), JsonUtil.toJson(metadata));
	}

	@Override
	public TaskType supports() {
		return TaskType.PLAY;
	}

	@Override
	public Class<? extends TaskCreateRequest> supportsClass() {
		return PlayTaskCreateRequest.class;
	}
}
