package kr.co.jeelee.kiwee.domain.reputations.entity;

import java.time.YearMonth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "reputation_stats",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_reputation_stats", columnNames = {"member_id", "year_month"})
	}
)
@Getter
@NoArgsConstructor
public class ReputationStats extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member member;

	@Column(nullable = false)
	private YearMonth yearMonth;

	@Column(nullable = false)
	private int upCount;

	@Column(nullable = false)
	private int downCount;

	@Column(nullable = false)
	private int netScore;

	@Column(nullable = false)
	private int rank;

	private ReputationStats(Member member, YearMonth yearMonth, int upCount, int downCount, int rank) {
		this.member = member;
		this.yearMonth = yearMonth;
		this.upCount = upCount;
		this.downCount = downCount;
		this.netScore = upCount - downCount;
		this.rank = rank;
	}

	public static ReputationStats of(Member member, YearMonth yearMonth, int upCount, int downCount, int rank) {
		return new ReputationStats(member, yearMonth, upCount, downCount, rank);
	}

}
