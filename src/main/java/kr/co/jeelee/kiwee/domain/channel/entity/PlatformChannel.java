package kr.co.jeelee.kiwee.domain.channel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "platform_channels",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_platform_channel",
			columnNames = {"platform_id", "platform_channel_id"}
		)
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlatformChannel extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String platformChannelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "platform_id")
	private Platform platform;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	private PlatformChannel(String platformChannelId, Platform platform, Channel channel) {
		this.platformChannelId = platformChannelId;
		this.platform = platform;
		this.channel = channel;
	}

	public static PlatformChannel of(String platformChannelId, Platform platform, Channel channel) {
		return new PlatformChannel(platformChannelId, platform, channel);
	}

}
