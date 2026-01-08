package com.guntinue.money.service;

import com.guntinue.money.dto.StatisticsResponse;
import com.guntinue.money.repository.MoneyLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 통계 서비스.
 * MoneyLog 데이터를 기반으로 각종 통계 데이터를 계산합니다.
 */
@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final MoneyLogRepository moneyLogRepository;

    public StatisticsService(MoneyLogRepository moneyLogRepository) {
        this.moneyLogRepository = moneyLogRepository;
    }

    /**
     * 시간대별 지출 합계 통계 조회.
     * 지정된 기간 내의 데이터를 시간(Hour) 단위로 그룹화하여 반환합니다.
     *
     * @param start 조회 시작 시간
     * @param end   조회 종료 시간
     * @return 시간대별 통계 리스트 (e.g., "12:00", 50000원)
     */
    public List<StatisticsResponse> getHourlyStatistics(LocalDateTime start, LocalDateTime end) {
        return moneyLogRepository.findHourlyStatistics(start, end);
    }
}
