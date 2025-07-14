package kr.co.jeelee.kiwee.domain.invite.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.invite.model.InviteStatus;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class InviteStatusConverter extends EnumToStringConverter<InviteStatus> {

	public InviteStatusConverter() {
		super(InviteStatus.class);
	}
}
