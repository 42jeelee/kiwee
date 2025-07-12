package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class PermissionTypeConverter extends EnumToStringConverter<PermissionType> {

	public PermissionTypeConverter() {
		super(PermissionType.class);
	}
}
