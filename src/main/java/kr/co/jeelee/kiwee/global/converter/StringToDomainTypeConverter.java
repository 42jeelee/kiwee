package kr.co.jeelee.kiwee.global.converter;

import org.springframework.core.convert.converter.Converter;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public class StringToDomainTypeConverter implements Converter<String, DomainType> {

	@Override
	public DomainType convert(String source) {
		return DomainType.from(source);
	}
}
