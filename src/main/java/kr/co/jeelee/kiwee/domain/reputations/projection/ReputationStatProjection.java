package kr.co.jeelee.kiwee.domain.reputations.projection;

import java.util.UUID;

public interface ReputationStatProjection {
	UUID getId();
	String getNickname();
	String getAvatarUrl();
	int getLevel();
	long getExp();

	int getUpVotes();
	int getDownVotes();
	int getNetScore();
	int getRank();
}
