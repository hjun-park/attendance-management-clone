package com.hjun.timereport.performance;

import java.util.List;

import com.hjun.timereport.performance.dto.ApprovalDto;
import com.hjun.timereport.performance.dto.ApprovalReq;

public interface ApprovalService {

	List<ApprovalDto> findApprovals(String day, Long memberId);

	List<Long> approvalAny(ApprovalReq approvalReq, Long memberId);

	List<Long> cancelApprovalAny(ApprovalReq approvalReq, Long memberId);

	List<Long> resetApprovalAny(ApprovalReq approvalReq, Long memberId);

}
