package com.smd.budgetman.services;

import com.smd.budgetman.vo.ExpendsResponseVo;
import com.smd.budgetman.vo.ExpendsVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExpendsService {

    public ExpendsResponseVo getExpendsById(Long expendsId) {
        return null;
    }

    public void createNewExpends(ExpendsVo expends) {}

    public List<ExpendsResponseVo> getExpendsByBudgetId(Long budgetId) {
        return null;
    }

    public void deleteExpends(Long expendsId) {}
}
