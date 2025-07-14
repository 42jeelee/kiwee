package kr.co.jeelee.kiwee.global.dto.response.common;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record PagedResponse<T>(
	List<T> content,
	int page,
	int size,
	long totalElements,
	int totalPages,
	boolean last
) {

	public static <T, R> PagedResponse<R> of(Page<T> page, Function<T, R> mapper) {
		List<R> content = page.getContent().stream()
			.map(mapper)
			.toList();

		return new PagedResponse<>(
			content,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.isLast()
		);
	}

}
