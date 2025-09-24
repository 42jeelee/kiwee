package kr.co.jeelee.kiwee.domain.content.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public interface ContentRepository extends JpaRepository<Content, UUID>, ContentQueryRepository {

	@Query(value = """
        WITH RECURSIVE parent_chain AS (
            SELECT id, parent_id FROM contents WHERE id = :id
            UNION ALL
            SELECT c.id, c.parent_id FROM contents c
            INNER JOIN parent_chain pc ON c.id = pc.parent_id
        )
        SELECT id FROM parent_chain WHERE parent_id IS NULL
	""", nativeQuery = true)
	Optional<UUID> findRootByChildrenId(@Param("id") UUID id);

	Page<Content> findByParent(Content parent, Pageable pageable);

	Page<Content> findDistinctByGenres_idIn(Set<Long> genresIds, Pageable pageable);

	Page<Content> findByContentTypeIn(Set<ContentType> contentTypes, Pageable pageable);

	Page<Content> findDistinctByContentTypeInAndGenres_idIn(Set<ContentType> contentTypes, Set<Long> genresIds, Pageable pageable);

}
