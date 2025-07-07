package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.model.MediaType;

@Converter(autoApply = true)
public class ContentTypeConverter extends EnumToStringConverter<MediaType> {

	public ContentTypeConverter() {
		super(MediaType.class);
	}
}
