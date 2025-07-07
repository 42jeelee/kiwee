package kr.co.jeelee.kiwee.domain.content.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface ContentService {

	ContentDetailResponse createContent(ContentCreateRequest request);

	ContentDetailResponse getContentDetail(UUID id);

	PagedResponse<ContentSimpleResponse> getContents(ContentType contentType, Set<Long> genreIds, Pageable pageable);

	PagedResponse<ContentSimpleResponse> getChildren(UUID id, Pageable pageable);

	Content getById(UUID id);

}
