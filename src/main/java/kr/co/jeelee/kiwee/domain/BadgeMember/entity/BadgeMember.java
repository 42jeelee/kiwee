package kr.co.jeelee.kiwee.domain.BadgeMember.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badge_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeMember extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "badge_id", nullable = false)
	private Badge badge;

	@Column(nullable = false)
	private Integer level;

	private BadgeMember(Member member, Badge badge) {
		this.member = member;
		this.badge = badge;
		this.level = 1;
	}

	public static BadgeMember of(Member member, Badge badge) {
		return new BadgeMember(member, badge);
	}

	public boolean levelUp() {
		if (this.badge.getMaxLevel() == 0 || this.level < this.badge.getMaxLevel()) {
			this.level++;
			return true;
		}
		return false;
	}

}
