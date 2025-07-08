package kr.co.jeelee.kiwee.domain.task.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class TaskTypeConverter extends EnumToStringConverter<TaskType> {

	public TaskTypeConverter() {
		super(TaskType.class);
	}
}
