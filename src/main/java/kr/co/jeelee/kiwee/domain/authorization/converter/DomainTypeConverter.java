package kr.co.jeelee.kiwee.domain.authorization.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class DomainTypeConverter extends EnumToStringConverter<DomainType> {

	public DomainTypeConverter() {
		super(DomainType.class);
	}
}
