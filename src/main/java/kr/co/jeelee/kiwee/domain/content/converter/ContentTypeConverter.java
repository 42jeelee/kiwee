package kr.co.jeelee.kiwee.domain.content.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class ContentTypeConverter extends EnumToStringConverter<ContentType> {

	public ContentTypeConverter() {
		super(ContentType.class);
	}
}
