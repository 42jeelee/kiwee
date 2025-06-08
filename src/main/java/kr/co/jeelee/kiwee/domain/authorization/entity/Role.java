package kr.co.jeelee.kiwee.domain.authorization.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Role extends BaseTimeEntity {

	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(length = 30, nullable = false)
	private DomainType domain;

	@Column(length = 30, unique = true, nullable = false)
	private RoleType name;

	@Column(length = 8, nullable = false)
	private String color;

	@Column
	private String description;

	@ManyToMany
	@JoinTable(
		name = "role_permissions",
		joinColumns = @JoinColumn(name = "role_id"),
		inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private Set<Permission> permissions;

	private Role(RoleType name, DomainType domain, String color, String description) {
		this.name = name;
		this.domain = domain;
		this.color = color;
		this.description = description;
		this.permissions = new HashSet<>();
	}

	public static Role of(RoleType name, DomainType domain, String color, String description) {
		return new Role(name, domain, color, description);
	}

	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}

	public void removePermission(Permission permission) {
		this.permissions.remove(permission);
	}

}
