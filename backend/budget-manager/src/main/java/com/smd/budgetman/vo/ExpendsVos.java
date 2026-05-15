package com.smd.budgetman.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ExpendsVos {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpendsVo {
        private String expendsName;
        private String commentary;
        private Double amount;
        private Long budgetId;
        private Long paidById;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpendsSimpleVo {
        private Long expendsId;
        private String expendsName;
        private Double amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpendsResponseVo {
        private Long expendsId;
        private String expendsName;
        private String commentary;
        private Double amount;
        private UserVos.UserVo paidBy;
        private BudgetVos.BudgetSimpleVo budget;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
