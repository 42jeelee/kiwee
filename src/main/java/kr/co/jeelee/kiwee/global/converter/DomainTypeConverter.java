package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class DomainTypeConverter extends EnumToStringConverter<DomainType> {

	public DomainTypeConverter() {
		super(DomainType.class);
	}
}
