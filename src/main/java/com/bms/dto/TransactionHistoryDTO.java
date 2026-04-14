package com.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDTO {
    private Long bookingId;
    private String eventTitle;
    private double amount;
    private String status;
    private LocalDateTime timestamp;
}
