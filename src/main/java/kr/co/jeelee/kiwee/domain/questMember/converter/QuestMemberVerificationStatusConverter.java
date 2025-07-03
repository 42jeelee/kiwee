package kr.co.jeelee.kiwee.domain.questMember.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberVerificationStatus;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class QuestMemberVerificationStatusConverter extends EnumToStringConverter<QuestMemberVerificationStatus> {

	public QuestMemberVerificationStatusConverter() {
		super(QuestMemberVerificationStatus.class);
	}
}
