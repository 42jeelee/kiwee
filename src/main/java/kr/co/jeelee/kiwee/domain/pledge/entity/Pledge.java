package kr.co.jeelee.kiwee.domain.pledge.entity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pledges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pledge extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proposer_id", nullable = false)
	private Member proposer;


	@OneToMany(mappedBy = "pledge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PledgeRule> rules;

	@Column
	private Duration completedLimit;

	@Column(nullable = false)
	private TermType termType;

	private Pledge(
		String title, String description, Member proposer, Duration completedLimit, TermType termType
	) {
		this.title = title;
		this.description = description;
		this.proposer = proposer;
		this.rules = new ArrayList<>();
		this.completedLimit = completedLimit;
		this.termType = termType;
	}

	public static Pledge of(
		String title, String description, Member proposer, Duration completedLimit, TermType termType
	) {
		return new Pledge(
			title, description, proposer, completedLimit, termType
		);
	}

	public void addRule(PledgeRule rule) {
		this.rules.add(rule);
		rule.setPledge(this);
	}

	public boolean isIncludeCriterion(ActivityCriterion activityCriterion) {
		return this.rules.stream()
			.anyMatch(r -> r.getCriterion().equals(activityCriterion));
	}

	public PledgeRule getRuleByCriterion(ActivityCriterion criterion) {
		return this.rules.stream()
			.filter(r -> r.getCriterion().equals(criterion))
			.findFirst()
			.orElse(null);
	}

}
