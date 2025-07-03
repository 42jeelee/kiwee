package kr.co.jeelee.kiwee.domain.questMember.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class QuestMemberStatusConverter extends EnumToStringConverter<QuestMemberStatus> {

	public QuestMemberStatusConverter() {
		super(QuestMemberStatus.class);
	}
}
