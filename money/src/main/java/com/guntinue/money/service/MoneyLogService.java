package com.guntinue.money.service;

import com.guntinue.money.domain.MoneyLog;
import com.guntinue.money.dto.MoneyLogRequest;
import com.guntinue.money.dto.MoneyLogResponse;
import com.guntinue.money.repository.MoneyLogRepository;
import com.guntinue.user.domain.Member;
import com.guntinue.user.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MoneyLogService {

    private final MoneyLogRepository moneyLogRepository;
    private final MemberRepository memberRepository;

    public MoneyLogService(MoneyLogRepository moneyLogRepository, MemberRepository memberRepository) {
        this.moneyLogRepository = moneyLogRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long createLog(Long memberId, MoneyLogRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        MoneyLog log = new MoneyLog(
                request.getTitle(),
                request.getAmount(),
                request.getDescription(),
                request.getCategory(),
                request.getTransactionTime(),
                member);
        moneyLogRepository.save(log);
        return log.getId();
    }

    public List<MoneyLogResponse> getMonthlyLogs(Long memberId, String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth); // YYYY-MM
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        return moneyLogRepository
                .findByMemberIdAndTransactionTimeBetweenOrderByTransactionTimeDesc(memberId, start, end)
                .stream().map(MoneyLogResponse::new).collect(Collectors.toList());
    }

    public List<MoneyLogResponse> getRecentLogs(Long memberId) {
        return moneyLogRepository.findTop5ByMemberIdOrderByTransactionTimeDesc(memberId)
                .stream().map(MoneyLogResponse::new).collect(Collectors.toList());
    }
}
