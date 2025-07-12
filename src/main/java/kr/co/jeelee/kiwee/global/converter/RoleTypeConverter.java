package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.model.RoleType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class RoleTypeConverter extends EnumToStringConverter<RoleType> {

	public RoleTypeConverter() {
		super(RoleType.class);
	}
}
