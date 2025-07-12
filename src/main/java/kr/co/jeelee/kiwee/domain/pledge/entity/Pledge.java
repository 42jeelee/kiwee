package kr.co.jeelee.kiwee.domain.pledge.entity;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
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

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "pledge_criteria",
		joinColumns = @JoinColumn(name = "pledge_id")
	)
	@AttributeOverrides({
		@AttributeOverride(name = "domainType", column = @Column(name = "domain_type", nullable = false)),
		@AttributeOverride(name = "domainId", column = @Column(name = "domain_id")),
		@AttributeOverride(name = "activityType", column = @Column(name = "activity_type", nullable = false)),
		@AttributeOverride(name = "activityCount", column = @Column(name = "activity_count", nullable = false))
	})
	private List<ActivityCriterion> criteria;

	@Column(nullable = false)
	private Duration completedLimit;

	@Column(nullable = false)
	private TermType termType;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private RepeatCondition condition;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "pledge_repeat_fields", joinColumns = @JoinColumn(name = "pledge_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "allowed_custom_fields")
	private Set<RepeatConditionField> allowedCustomFields;


	private Pledge(
		String title, String description, Member proposer, List<ActivityCriterion> criteria,
		Duration completedLimit, TermType termType,
		RepeatCondition condition, Set<RepeatConditionField> allowedCustomFields
	) {
		this.title = title;
		this.description = description;
		this.proposer = proposer;
		this.criteria = criteria;
		this.completedLimit = completedLimit;
		this.termType = termType;
		this.condition = condition;
		this.allowedCustomFields = allowedCustomFields;
	}

	public static Pledge of(
		String title, String description, Member proposer, List<ActivityCriterion> criteria,
		Duration completedLimit, TermType termType,
		RepeatCondition condition, Set<RepeatConditionField> allowedCustomFields
	) {
		return new Pledge(
			title, description, proposer, criteria, completedLimit,
			termType, condition, allowedCustomFields
		);
	}

}
