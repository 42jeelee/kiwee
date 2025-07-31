package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;
import kr.co.jeelee.kiwee.global.model.RewardMatchPolicy;

@Converter(autoApply = true)
public class RewardMatchPolicyConverter extends EnumToStringConverter<RewardMatchPolicy> {

	public RewardMatchPolicyConverter() {
		super(RewardMatchPolicy.class);
	}
}
