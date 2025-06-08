package kr.co.jeelee.kiwee.domain.invite.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InviteStatus {
	PENDING,
	ACCEPTED,
	REJECTED,
	EXPIRED,
	;

	@JsonCreator
	public static InviteStatus from(String inviteStatus) {
		return InviteStatus.valueOf(inviteStatus.toUpperCase());
	}

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

}
