package kr.co.jeelee.kiwee.domain.member.entity;

import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
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
public class Member extends BaseTimeEntity {

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

	@Column(nullable = false)
	private boolean isBot;

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
		this.isBot = false;
		this.isActive = true;
	}

	public static Member of(String name, String nickname, String email, String avatarUrl) {
		return new Member(name, nickname, email, avatarUrl);
	}

	public void updateName(String name) {
		if (name == null || name.isBlank()) {
			throw new FieldValidationException("name", "이름은 비어있을 수 없습니다.");
		}
		if (name.length() > 20) {
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

	public void gainExp(long exp) {
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

	private long getLevelExp() {
		long baseExp = 50L;
		double increaseRate = 0.005;

		return (long)(increaseRate * Math.pow(this.level, 3) + (baseExp - increaseRate));
	}

}
