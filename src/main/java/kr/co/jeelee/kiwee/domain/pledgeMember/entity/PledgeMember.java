package kr.co.jeelee.kiwee.domain.pledgeMember.entity;

import java.time.LocalDate;
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
import jakarta.persistence.Transient;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.domain.pledgeMember.exception.PledgeMemberInvalidException;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.util.TermUtil;
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
	private Map<ActivityCriterion, RepeatCondition> conditions;

	@Transient
	private Map<ActivityCriterion, Integer> progress;

	private PledgeMember(
		Pledge pledge, Member member, LocalDateTime startAt, LocalDateTime completedAt,
		Map<ActivityCriterion, RepeatCondition> conditions
	) {
		this.pledge = pledge;
		this.member = member;
		this.status = PledgeStatusType.PLANNED;
		this.startAt = startAt;
		this.limitedAt = pledge.getCompletedLimit() != null
			? startAt.plus(pledge.getCompletedLimit())
			: null;
		this.completedAt = completedAt;
		this.conditions = conditions;
		this.progress = new HashMap<>();
	}

	public static PledgeMember of(
		Pledge pledge, Member member, LocalDateTime startAt, LocalDateTime completedAt,
		Map<ActivityCriterion, RepeatCondition> conditions
	) {
		return new PledgeMember(pledge, member, startAt, completedAt, conditions);
	}

	public RepeatCondition getCondition(ActivityCriterion criterion) {
		return criterion != null ? conditions.get(criterion) : null;
	}

	public void loadProgress(Map<ActivityCriterion, Integer> progress) {
		progress.keySet().forEach(criterion -> {
			boolean exists = this.pledge.getRules().stream()
				.anyMatch(r -> r.getCriterion().equals(criterion));

			if (!exists) {
				throw new PledgeMemberInvalidException("존재하지 않는 조건이 포함되어 있습니다.");
			}
		});
		this.progress = progress;
	}

	public void inProgress() {
		assertPlanned();
		this.status = PledgeStatusType.IN_PROGRESS;
	}

	public boolean isClearAllCriteria() {
		return this.pledge.getRules().stream()
			.allMatch(r -> {
				if (r.getCondition() != null && this.pledge.getTermType() == TermType.DAILY) {
					RepeatCondition condition = this.conditions != null
						? r.getCondition().mergeWith(this.conditions.get(r.getCriterion()))
						: r.getCondition();

					if (!TermUtil.isTodayMatched(condition, LocalDate.now())) {
						return true;
					}
				}
				int count = this.progress.getOrDefault(r.getCriterion(), 0);

				return r.getCriterion().getActivityCount() <= count;
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

	public void success() {
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
