package kr.co.jeelee.kiwee.domain.pledge.entity;

import java.util.Set;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pledge_rules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PledgeRule {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledge_id", nullable = false)
	private Pledge pledge;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "domainType", column = @Column(name = "domain_type", nullable = false)),
		@AttributeOverride(name = "domainId", column = @Column(name = "domain_id")),
		@AttributeOverride(name = "activityType", column = @Column(name = "activity_type", nullable = false)),
		@AttributeOverride(name = "activityCount", column = @Column(name = "activity_count", nullable = false))
	})
	private ActivityCriterion criterion;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	RepeatCondition condition;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	Set<RepeatConditionField> allowedCustomFields;

	private PledgeRule(
		Pledge pledge, ActivityCriterion criterion, RepeatCondition condition,
		Set<RepeatConditionField> allowedCustomFields
	) {
		this.pledge = pledge;
		this.criterion = criterion;
		this.condition = condition;
		this.allowedCustomFields = allowedCustomFields;
	}

	public static PledgeRule of(
		Pledge pledge, ActivityCriterion criterion, RepeatCondition condition,
		Set<RepeatConditionField> allowedCustomFields
	) {
		return new PledgeRule(pledge, criterion, condition, allowedCustomFields);
	}

}
