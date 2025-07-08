package kr.co.jeelee.kiwee.domain.task.creator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.task.dto.request.ReviewTaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.metadata.ReviewMetadata;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewTaskCreator implements TaskCreator<ReviewTaskCreateRequest> {

	private final ChannelService channelService;
	private final MemberService memberService;

	@Override
	public Task create(UUID channelId, UUID memberId, ReviewTaskCreateRequest request) {
		Channel channel = channelId != null
			? channelService.getById(channelId)
			: null;

		Member member = memberService.getById(memberId);
		ReviewMetadata metadata = request.metadata();

		return Task.of(channel, member, supports(), JsonUtil.toJson(metadata));
	}

	@Override
	public TaskType supports() {
		return  TaskType.REVIEW;
	}

	@Override
	public Class<? extends TaskCreateRequest> supportsClass() {
		return ReviewTaskCreateRequest.class;
	}
}
