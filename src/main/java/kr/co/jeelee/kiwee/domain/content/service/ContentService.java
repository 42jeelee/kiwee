package kr.co.jeelee.kiwee.domain.content.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateWithPlatformRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentUpdateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface ContentService {

	ContentDetailResponse createContent(ContentCreateRequest request);

	ContentDetailResponse createContent(UUID platformId, ContentCreateWithPlatformRequest request);

	ContentDetailResponse getContentDetail(UUID id);

	PagedResponse<ContentSimpleResponse> getContents(Set<ContentType> contentTypes, Set<Long> genreIds, Pageable pageable);

	PagedResponse<ContentSimpleResponse> getContentsByParentId(UUID parentId, Pageable pageable);

	ContentDetailResponse updateContent(UUID id, ContentUpdateRequest request);

	void deleteContent(UUID id);

	Content getById(UUID id);

	Content getRootById(UUID id);

	Content getByPlatform(UUID platformId, String idInPlatform);

	UUID getContentIdByPlatform(UUID platformId, String idInPlatform);

}
