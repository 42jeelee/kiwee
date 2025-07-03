package kr.co.jeelee.kiwee.domain.quest.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.quest.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, UUID> {

	Page<Quest> findByChannelId(UUID channelId, Pageable pageable);

}
