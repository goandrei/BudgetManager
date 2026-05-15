package com.smd.budgetman.controller;

import com.smd.budgetman.services.BudgetService;
import com.smd.budgetman.vo.BudgetVos.BudgetResponseVo;
import com.smd.budgetman.vo.BudgetVos.BudgetVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @GetMapping("/budget/{budgetId}")
    @CrossOrigin
    public ResponseEntity<BudgetResponseVo> getBudgetById(@PathVariable Long budgetId) {
        BudgetResponseVo budget = budgetService.getBudgetById(budgetId);
        if (budget != null) {
            return ResponseEntity.ok(budget);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/budget/creat/{userId}")
    @CrossOrigin
    public ResponseEntity<List<BudgetResponseVo>> getBudgetByCreator(@PathVariable Long userId) {
        List<BudgetResponseVo> budget = budgetService.getBudgetByCreator(userId);
        if (budget != null) {
            return ResponseEntity.ok(budget);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/budget/create")
    public void createNewBudget(@RequestBody BudgetVo budget) {
        budgetService.createNewBudget(budget);
    }

    @DeleteMapping("/budget/delete/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }
}
