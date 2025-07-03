package kr.co.jeelee.kiwee.domain.Reward.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class TriggerTypeConverter extends EnumToStringConverter<TriggerType> {

	public TriggerTypeConverter() {
		super(TriggerType.class);
	}
}
