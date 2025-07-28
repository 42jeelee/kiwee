package kr.co.jeelee.kiwee.domain.reward.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class RewardTypeConverter extends EnumToStringConverter<RewardType> {

	public RewardTypeConverter() {
		super(RewardType.class);
	}
}
