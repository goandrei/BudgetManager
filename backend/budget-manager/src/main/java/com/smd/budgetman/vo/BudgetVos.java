package com.smd.budgetman.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BudgetVos {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetVo {
        private String budgetName;
        private String commentary;
        private Long userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetSimpleVo {
        private Long budgetId;
        private String budgetName;
        private String commentary;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetResponseVo {
        private Long budgetId;
        private String budgetName;
        private String commentary;
        private UserVos.UserVo user;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
