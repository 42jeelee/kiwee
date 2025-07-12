package kr.co.jeelee.kiwee.domain.channelMember.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.global.model.RoleType;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channel_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelMember extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	private Boolean isBen;

	@OneToMany(mappedBy = "channelMember", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ChannelMemberRole> roles;

	private ChannelMember(Channel channel, Member member) {
		this.channel = channel;
		this.member = member;
		this.isBen = false;
		this.roles = new HashSet<>();
	}

	public static ChannelMember of(Channel channel, Member member) {
		return new ChannelMember(channel, member);
	}

	public void ben() {
		this.isBen = true;
	}

	public void unBen() {
		this.isBen = false;
	}

	public ChannelMemberRole getRole(RoleType roleType) {
		return this.roles.stream()
			.filter(cmr -> cmr.getRole().getName().equals(roleType))
			.findFirst().orElse(null);
	}

	public void addRole(ChannelMemberRole role) {
		this.roles.add(role);
	}

	public void addRoles(Set<ChannelMemberRole> roles) {
		this.roles.addAll(roles);
	}

	public void removeRole(ChannelMemberRole role) {
		this.roles.remove(role);
	}

}
