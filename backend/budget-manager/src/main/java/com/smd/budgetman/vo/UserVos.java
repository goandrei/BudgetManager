package com.smd.budgetman.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserVos {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserVo {
        private Long userId;
        private String userName;
        private String phoneNumber;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserTokenResponseVo {
        private Long userId;
        private String token;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateVo {
        private String username;
        private String phoneNumber;
        private String oldPassword;
        private String newPassword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRequestVo {
        private String username;
        private String email;
        private String password;
        private String phoneNumber;
        private String token;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginRequestVo {
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAuthorizeRequestVo {
        private String token;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAuthorizeResponseVo {
        private Long userId;
        private boolean isValid;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBudgetResponseVo {
        private Long userBudgetId;
        private UserVo user;
        private BudgetVos.BudgetSimpleVo budget;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBudgetVo {
        private String phoneNumber;
        private Long budgetId;
    }
}
