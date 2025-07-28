package kr.co.jeelee.kiwee.domain.contentMember.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public interface ContentMemberRepository extends JpaRepository<ContentMember, Long> {

	@EntityGraph(attributePaths = "reviews")
	Optional<ContentMember> findByContentAndMember(Content content, Member member);

	Page<ContentMember> findByContent(Content content, Pageable pageable);
	Page<ContentMember> findByMember(Member member, Pageable pageable);
	Page<ContentMember> findByContentAndMember(Content content, Member member, Pageable pageable);

}
