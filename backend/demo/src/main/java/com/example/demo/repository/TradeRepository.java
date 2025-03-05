package com.example.demo.repository;

import com.example.demo.model.Trade;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TradeRepository extends BaseRepository<Trade, Long> {

    @Query("SELECT t FROM Trade t WHERE " +
            "(t.initiator.id = :userId OR t.receiver.id = :userId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:startDate IS NULL OR t.tradeDate >= :startDate) AND " +
            "(:endDate IS NULL OR t.tradeDate <= :endDate)")
    List<Trade> findTradesForUserWithFilters(
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT t FROM Trade t WHERE t.initiator = :user OR t.receiver = :user ORDER BY t.tradeDate DESC")
    List<Trade> findRecentTradesByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT t FROM Trade t WHERE t.initiator = :user OR t.receiver = :user ORDER BY t.tradeDate DESC")
    List<Trade> findTradesForUser(@Param("user") User user);

    boolean existsByInitiatorUsernameAndItemId(String username, Long itemId);


}
