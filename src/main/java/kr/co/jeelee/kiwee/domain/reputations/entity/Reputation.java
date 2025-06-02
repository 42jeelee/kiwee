package kr.co.jeelee.kiwee.domain.reputations.entity;

import java.time.YearMonth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "reputations",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_reputation_giver_month", columnNames = {"giver_id", "year_month"})
	},
	indexes = {
		@Index(name = "idx_reputation_receiver_month", columnList = "receiver_id, year_month")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reputation extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member giver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member receiver;

	@Column(nullable = false)
	private YearMonth yearMonth;

	@Column(nullable = false)
	private boolean isUp;

	private Reputation(Member giver, Member receiver, boolean isUp) {
		this.giver = giver;
		this.receiver = receiver;
		this.yearMonth = YearMonth.now();
		this.isUp = isUp;
	}

	public static Reputation of(Member giver, Member receiver, boolean isUp) {
		return new Reputation(giver, receiver, isUp);
	}
}
