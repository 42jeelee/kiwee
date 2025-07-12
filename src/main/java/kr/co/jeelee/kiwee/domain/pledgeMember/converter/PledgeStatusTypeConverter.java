package kr.co.jeelee.kiwee.domain.pledgeMember.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class PledgeStatusTypeConverter extends EnumToStringConverter<PledgeStatusType> {

	public PledgeStatusTypeConverter() {
		super(PledgeStatusType.class);
	}
}
