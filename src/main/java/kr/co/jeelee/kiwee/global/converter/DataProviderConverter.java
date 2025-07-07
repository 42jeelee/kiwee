package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.model.DataProvider;

@Converter(autoApply = true)
public class DataProviderConverter extends EnumToStringConverter<DataProvider> {

	public DataProviderConverter() {
		super(DataProvider.class);
	}
}
