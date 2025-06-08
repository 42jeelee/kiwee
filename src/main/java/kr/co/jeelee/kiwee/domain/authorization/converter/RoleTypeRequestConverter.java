package kr.co.jeelee.kiwee.domain.authorization.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;

@Component
public class RoleTypeRequestConverter implements Converter<String, RoleType> {

	@Override
	public RoleType convert(String source) {
		return RoleType.from(source);
	}
}