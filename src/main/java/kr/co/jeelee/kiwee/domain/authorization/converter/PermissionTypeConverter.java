package kr.co.jeelee.kiwee.domain.authorization.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class PermissionTypeConverter extends EnumToStringConverter<PermissionType> {

	public PermissionTypeConverter() {
		super(PermissionType.class);
	}
}
