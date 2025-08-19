package kr.co.jeelee.kiwee.global.dto.response;

import java.util.UUID;

public record OnlyIdResponse(
	UUID id
) {
	public static OnlyIdResponse from(UUID id) {
		return new OnlyIdResponse(id);
	}
}
