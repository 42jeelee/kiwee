package kr.co.jeelee.kiwee.domain.authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public interface RoleRepository extends JpaRepository<Role, UUID> {

	boolean existsByName(String name);

	Optional<Role> findByName(String name);

	@Query("""
		SELECT r FROM Role r
		JOIN FETCH r.permissions
		WHERE r.name = :name
	""")
	Optional<Role> getWithPermissionsByName(@Param("name") String name);

	Page<Role> findByDomain(DomainType domain, Pageable pageable);

}
