package kr.co.jeelee.kiwee.domain.Reward.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class RewardTypeConverter extends EnumToStringConverter<RewardType> {

	public RewardTypeConverter() {
		super(RewardType.class);
	}
}
