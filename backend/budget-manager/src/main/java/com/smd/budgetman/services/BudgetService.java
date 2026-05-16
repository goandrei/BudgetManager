package com.smd.budgetman.services;

import com.smd.budgetman.model.Budget;
import com.smd.budgetman.model.Expends;
import com.smd.budgetman.model.User;
import com.smd.budgetman.model.UserBudget;
import com.smd.budgetman.repository.BudgetRepository;
import com.smd.budgetman.repository.ExpendsRepository;
import com.smd.budgetman.repository.UserBudgetRepository;
import com.smd.budgetman.repository.UserRepository;
import com.smd.budgetman.vo.BudgetVos.BudgetResponseVo;
import com.smd.budgetman.vo.BudgetVos.BudgetVo;
import com.smd.budgetman.vo.UserVos.UserVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final UserBudgetRepository userBudgetRepository;
    private final ExpendsRepository expendsRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            UserBudgetRepository userBudgetRepository,
            ExpendsRepository expendsRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.userBudgetRepository = userBudgetRepository;
        this.expendsRepository = expendsRepository;
    }

    public BudgetResponseVo getBudgetById(Long budgetId) {
        Budget budget = budgetRepository.findByBudgetId(budgetId);

        if (budget == null) {
            throw new IllegalArgumentException("Budget not found: " + budgetId);
        }

        return toResponseVo(budget);
    }

    public List<BudgetResponseVo> getBudgetByCreator(Long userId) {
        List<Budget> budget = budgetRepository.findByUser_UserId(userId);
        return budget.stream().map(this::toResponseVo).toList();
    }

    public Budget createNewBudget(BudgetVo budgetVo) {

        if (budgetVo.getBudgetName() == null || budgetVo.getBudgetName().isBlank()) {
            throw new IllegalArgumentException("Budget name is required");
        }

        if (budgetVo.getUserId() == null) {
            throw new IllegalArgumentException("UserID is required");
        }

        User user = userRepository
                .findById(budgetVo.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + budgetVo.getUserId()));

        Budget budget = Budget.builder()
                .budgetName(budgetVo.getBudgetName())
                .commentary(budgetVo.getCommentary())
                .user(user)
                .build();

        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long budgetId) {
        List<UserBudget> ub = userBudgetRepository.findByBudgetId_BudgetId(budgetId);

        if (!ub.isEmpty()) {
            userBudgetRepository.deleteAll(ub);
        }

        List<Expends> e = expendsRepository.findByBudget_BudgetId(budgetId);

        if (!e.isEmpty()) {
            expendsRepository.deleteAll(e);
        }

        if (budgetRepository.existsById(budgetId)) {
            budgetRepository.deleteById(budgetId);
        } else {
            throw new IllegalArgumentException("Budget not found: " + budgetId);
        }
    }

    public void deleteBudgetByUserId(Long userId) {
        List<UserBudget> ub = userBudgetRepository.findByUserId_UserId(userId);

        if (!ub.isEmpty()) {
            userBudgetRepository.deleteAll(ub);
        }

        List<Budget> b = budgetRepository.findByUser_UserId(userId);

        if (!b.isEmpty()) {
            budgetRepository.deleteAll(b);
        }
    }

    private BudgetResponseVo toResponseVo(Budget budget) {
        return BudgetResponseVo.builder()
                .budgetId(budget.getBudgetId())
                .budgetName(budget.getBudgetName())
                .commentary(budget.getCommentary())
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .user(UserVo.builder()
                        .userId(budget.getUser().getUserId())
                        .userName(budget.getUser().getUserName())
                        .email(budget.getUser().getEmail())
                        .phoneNumber(budget.getUser().getPhoneNumber())
                        .build())
                .build();
    }
}
