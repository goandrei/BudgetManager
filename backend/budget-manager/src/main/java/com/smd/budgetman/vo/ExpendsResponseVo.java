package com.smd.budgetman.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpendsResponseVo {
    private Long expendsId;
    private String expendsName;
    private String commentary;
    private Double amount;
    private UserVo paidBy;
    private BudgetSimpleVo budget;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
