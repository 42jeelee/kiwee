package kr.co.jeelee.kiwee.domain.pledgeMember.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pledge_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PledgeMember extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledge_id", nullable = false)
	private Pledge pledge;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PledgeStatusType status;

	@Column(nullable = false)
	private LocalDateTime startAt;

	@Column
	private LocalDateTime limitedAt;

	@Column
	private LocalDateTime completedAt;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private Map<ActivityCriterion, Integer> progress;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private RepeatCondition condition;

	private PledgeMember(Pledge pledge, Member member, LocalDateTime startAt, RepeatCondition condition) {
		this.pledge = pledge;
		this.member = member;
		this.status = PledgeStatusType.PLANNED;
		this.startAt = startAt;
		this.limitedAt = pledge.getCompletedLimit() != null
			? startAt.plus(pledge.getCompletedLimit())
			: null;
		this.completedAt = null;
		this.progress = new HashMap<>();
		this.condition = condition;
	}

	public static PledgeMember of(Pledge pledge, Member member, LocalDateTime startAt, RepeatCondition condition) {
		return new PledgeMember(pledge, member, startAt, condition);
	}

	public void inProgress() {
		assertPlanned();
		this.status = PledgeStatusType.IN_PROGRESS;
	}

	public void addProgress(ActivityCriterion activityCriterion) {
		assertInProgress();
		this.progress.merge(activityCriterion, 1, Integer::sum);

		if (isClearAllCriteria()) {
			success();
		}
	}

	public boolean isClearAllCriteria() {
		return this.pledge.getCriteria().stream()
			.allMatch(c -> {
				int count = this.progress.getOrDefault(c, 0);
				return c.activityCount() <= count;
			});
	}

	public void fail() {
		assertInProgress();
		this.status = PledgeStatusType.FAILED;
		this.completedAt = LocalDateTime.now();
	}

	public void delay(LocalDateTime startAt) {
		assertPlanned();
		this.startAt = startAt;
	}

	private boolean isExpired() {
		return this.limitedAt != null && this.limitedAt.isBefore(LocalDateTime.now());
	}

	private void success() {
		this.status = PledgeStatusType.SUCCESS;
		this.completedAt = LocalDateTime.now();
	}

	private void assertInProgress() {
		if (isExpired() || this.status != PledgeStatusType.IN_PROGRESS) {
			throw new AccessDeniedException("실행 중인 약속이 아닙니다.");
		}
	}

	private void assertPlanned() {
		if (isExpired() || this.status != PledgeStatusType.PLANNED) {
			throw new AccessDeniedException("이미 시작됬거나 끝난 약속입니다.");
		}
	}

}
