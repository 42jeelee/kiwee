package kr.co.jeelee.kiwee.domain.content.repository;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public interface ContentRepository extends JpaRepository<Content, UUID> {

	Page<Content> findByParent(Content parent, Pageable pageable);

	Page<Content> findDistinctByGenres_idIn(Set<Long> genresIds, Pageable pageable);

	Page<Content> findByContentType(ContentType contentType, Pageable pageable);

	Page<Content> findDistinctByContentTypeAndGenres_idIn(ContentType contentType, Set<Long> genresIds, Pageable pageable);

}
