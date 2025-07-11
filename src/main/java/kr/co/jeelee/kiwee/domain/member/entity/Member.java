package kr.co.jeelee.kiwee.domain.member.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "members",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_member_nickname", columnNames = "nickname"),
		@UniqueConstraint(name = "uk_member_email", columnNames = "email")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Member extends BaseTimeEntity {

	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(length = 40, nullable = false)
	private String name;

	@Column(length = 20, nullable = false)
	private String nickname;

	@Column
	private String email;

	@Column(nullable = false)
	private int level;

	@Column(nullable = false)
	private long exp;

	@Column(nullable = false)
	private long totalExp;

	@Column
	private String avatarUrl;

	@ManyToMany
	@JoinTable(
		name = "member_roles",
		joinColumns = @JoinColumn(name = "member_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	@Column(nullable = false)
	private boolean isActive;

	private Member(String name, String nickname, String email, String avatarUrl) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.level = 0;
		this.exp = 0;
		this.totalExp = 0;
		this.avatarUrl = avatarUrl;
		this.roles = new HashSet<>();
		this.isActive = true;
	}

	public static Member of(String name, String nickname, String email, String avatarUrl) {
		return new Member(name, nickname, email, avatarUrl);
	}

	public void updateName(String name) {
		if (name == null || name.isBlank()) {
			throw new FieldValidationException("name", "이름은 비어있을 수 없습니다.");
		}
		if (name.length() > 40) {
			throw new FieldValidationException("name", "이름은 최대 40자까지 가능합니다.");
		}
		this.name = name;
	}

	public void updateNickname(String nickname) {
		if (nickname == null || nickname.isBlank()) {
			throw new FieldValidationException("nickname", "닉네임은 비어있을 수 없습니다.");
		}
		if (nickname.length() > 20) {
			throw new FieldValidationException("nickname", "닉네임은 최대 20자까지 가능합니다.");
		}
		this.nickname = nickname;
	}

	public void updateEmail(String email) {
		final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

		if (email == null || email.isBlank()) {
			throw new FieldValidationException("email", "이메일은 비어있을 수 없습니다.");
		}
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new FieldValidationException("email", "이메일 형식이 아닙니다.");
		}
		this.email = email;
	}

	public void updateAvatarUrl(String avatarUrl) {
		final Pattern IMAGE_PATTERN = Pattern.compile(
			"^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif|bmp|svg)$"
		);

		if (avatarUrl == null || avatarUrl.isBlank()) {
			throw new FieldValidationException("avatarUrl", "사진 URL은 비어있을 수 없습니다.");
		}
		if (!IMAGE_PATTERN.matcher(avatarUrl).matches()) {
			throw new FieldValidationException("avatarUrl", "사진 URL 형식이 아닙니다.");
		}
		this.avatarUrl = avatarUrl;
	}

	public void gainExp(int exp) {
		if (exp < 0) {
			throw new FieldValidationException("exp", "받는 경험치는 음수가 될 수 없습니다.");
		}
		long levelExp = getLevelExp();
		this.exp += exp;
		this.totalExp += exp;
		if (this.exp >= levelExp) {
			this.level += 1;
			this.exp -= levelExp;
			this.gainExp(0);
		}
	}

	public void addRole(Set<Role> role) {
		this.roles.addAll(role);
	}

	public void removeRole(Role role) {
		this.roles.remove(role);
	}

	private long getLevelExp() {
		long baseExp = 50L;
		double increaseRate = 0.005;

		return (long)(increaseRate * Math.pow(this.level, 3) + (baseExp - increaseRate));
	}

}
