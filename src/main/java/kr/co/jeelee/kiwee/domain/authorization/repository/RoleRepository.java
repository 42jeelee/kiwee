package kr.co.jeelee.kiwee.domain.authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;

public interface RoleRepository extends JpaRepository<Role, UUID> {

	@EntityGraph(attributePaths = "permissions")
	Optional<Role> findByName(RoleType name);

	Page<Role> findByDomain(DomainType domain, Pageable pageable);

}
