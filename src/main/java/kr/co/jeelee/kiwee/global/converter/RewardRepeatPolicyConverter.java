package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;
import kr.co.jeelee.kiwee.global.model.RewardRepeatPolicy;

@Converter(autoApply = true)
public class RewardRepeatPolicyConverter extends EnumToStringConverter<RewardRepeatPolicy> {

	public RewardRepeatPolicyConverter() {
		super(RewardRepeatPolicy.class);
	}
}
