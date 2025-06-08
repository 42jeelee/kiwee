package kr.co.jeelee.kiwee.domain.authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Permission extends BaseTimeEntity {

	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, nullable = false)
	private DomainType domain;

	@Column(length = 100, unique = true, nullable = false)
	private PermissionType name;

	@Column
	private String description;

	private Permission(DomainType domain, PermissionType name, String description) {
		this.domain = domain;
		this.name = name;
		this.description = description;
	}

	public static Permission of(DomainType domain, PermissionType name, String description) {
		return new Permission(domain, name, description);
	}

}
