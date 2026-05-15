package com.smd.budgetman.controller;

import com.smd.budgetman.services.ExpendsService;
import com.smd.budgetman.vo.ExpendsVos.ExpendsResponseVo;
import com.smd.budgetman.vo.ExpendsVos.ExpendsVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExpendsController {
    @Autowired
    private ExpendsService expendsService;

    @GetMapping("/expends/{expendsId}")
    @CrossOrigin
    public ResponseEntity<ExpendsResponseVo> getExpendsById(@PathVariable Long expendsId) {
        ExpendsResponseVo expends = expendsService.getExpendsById(expendsId);
        if (expends != null) {
            return ResponseEntity.ok(expends);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/expends/create")
    public void createNewExpends(@RequestBody ExpendsVo expends) {
        expendsService.createNewExpends(expends);
    }

    @GetMapping("/expends/budget/{budgetId}")
    public ResponseEntity<List<ExpendsResponseVo>> getExpendsBudget(@PathVariable Long budgetId) {
        List<ExpendsResponseVo> expends = expendsService.getExpendsByBudgetId(budgetId);
        return ResponseEntity.ok(expends);
    }

    @DeleteMapping("/expends/delete/{expendsId}")
    public ResponseEntity<Void> deleteExpends(@PathVariable Long expendsId) {
        expendsService.deleteExpends(expendsId);
        return ResponseEntity.noContent().build();
    }
}
