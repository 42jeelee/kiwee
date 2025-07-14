package kr.co.jeelee.kiwee.domain.pledgeMember.service;

import java.util.UUID;

public interface PledgeMemberStatusService {

	void progressActivity(UUID activityId);

	void autoEvaluateTime();
	void autoEvaluateDaily();
	void autoEvaluateWeekly();
	void autoEvaluateMonthly();
	void autoEvaluateYearly();

}
