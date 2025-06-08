package kr.co.jeelee.kiwee.domain.invite.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.invite.entity.Invite;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

	Boolean existsByCode(String code);
	Optional<Invite> findByCode(String code);

	Page<Invite> findByInviteeNull(Pageable pageable);

	Page<Invite> findByInviteeId(UUID inviteeId, Pageable pageable);

}
