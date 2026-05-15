package com.smd.budgetman.services;

import com.smd.budgetman.vo.UserVos.UserBudgetResponseVo;
import com.smd.budgetman.vo.UserVos.UserBudgetVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserBudgetService {

    public List<UserBudgetResponseVo> getUserBudgetByUserId(Long userId) {
        return null;
    }

    public List<UserBudgetResponseVo> getUserBudgetByBudgetId(Long budgetId) {
        return null;
    }

    public UserBudgetResponseVo getUserBudgetById(Long userBudgetId) {
        return null;
    }

    public void createUserBudget(UserBudgetVo userBudget) {}

    public void deleteUserBudget(Long userBudgetId) {}

    public void deleteUserPhoneBudget(UserBudgetVo userBudget) {}
}
