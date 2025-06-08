package kr.co.jeelee.kiwee.domain.authorization.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class RoleTypeConverter extends EnumToStringConverter<RoleType> {

	public RoleTypeConverter() {
		super(RoleType.class);
	}
}
