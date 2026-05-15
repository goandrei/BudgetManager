package com.smd.budgetman.services;

import com.smd.budgetman.vo.BudgetVos.BudgetResponseVo;
import com.smd.budgetman.vo.BudgetVos.BudgetVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    public BudgetResponseVo getBudgetById(Long budgetId) {
        return null;
    }

    public List<BudgetResponseVo> getBudgetByCreator(Long userId) {
        return null;
    }

    public void createNewBudget(BudgetVo budget) {}

    public void deleteBudget(Long budgetId) {}
}
