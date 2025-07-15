package kr.co.jeelee.kiwee.domain.task.entity;

import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false)
	private TaskType taskType;

	@Column(nullable = false)
	private ActivityType activityType;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private String metadata;

	private Task(Member member, TaskType taskType, ActivityType activityType, String metadata) {
		this.member = member;
		this.taskType = taskType;
		this.activityType = activityType;
		this.metadata = metadata;
	}

	public static Task of(Member member, TaskType taskType, ActivityType activityType, String metadata) {
		return new Task(member, taskType, activityType, metadata);
	}

}
