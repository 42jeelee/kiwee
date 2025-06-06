package kr.co.jeelee.kiwee.domain.authorization.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.authorization.entity.Permission;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Optional<Permission> findByName(PermissionType name);

	Page<Permission> findByDomain(DomainType domain, Pageable pageable);

}
