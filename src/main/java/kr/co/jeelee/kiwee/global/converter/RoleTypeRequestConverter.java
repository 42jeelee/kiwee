package kr.co.jeelee.kiwee.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


import kr.co.jeelee.kiwee.global.model.RoleType;

@Component
public class RoleTypeRequestConverter implements Converter<String, RoleType> {

	@Override
	public RoleType convert(String source) {
		return RoleType.from(source);
	}
}