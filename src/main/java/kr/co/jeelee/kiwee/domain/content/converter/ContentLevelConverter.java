package kr.co.jeelee.kiwee.domain.content.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.content.model.ContentLevel;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class ContentLevelConverter extends EnumToStringConverter<ContentLevel> {

	public ContentLevelConverter() {
		super(ContentLevel.class);
	}
}
