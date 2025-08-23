package kr.co.jeelee.kiwee.domain.memberTheme.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_themes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTheme extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, unique = true)
	private Member member;

	@Column(nullable = false)
	private String name;

	private MemberTheme(Member member, String name) {
		this.member = member;
		this.name = name;
	}

	public static MemberTheme of(Member member, String name) {
		return new MemberTheme(member, name);
	}

	public void changeName(String newName) {
		this.name = newName;
	}
}
