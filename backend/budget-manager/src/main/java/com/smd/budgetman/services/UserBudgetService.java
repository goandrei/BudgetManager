package com.smd.budgetman.services;

import com.smd.budgetman.model.Budget;
import com.smd.budgetman.model.Expends;
import com.smd.budgetman.model.User;
import com.smd.budgetman.model.UserBudget;
import com.smd.budgetman.repository.BudgetRepository;
import com.smd.budgetman.repository.ExpendsRepository;
import com.smd.budgetman.repository.UserBudgetRepository;
import com.smd.budgetman.repository.UserRepository;
import com.smd.budgetman.vo.BudgetVos.BudgetSimpleVo;
import com.smd.budgetman.vo.UserVos.UserBudgetResponseVo;
import com.smd.budgetman.vo.UserVos.UserBudgetVo;
import com.smd.budgetman.vo.UserVos.UserVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserBudgetService {
    private final UserBudgetRepository userBudgetRepository;
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ExpendsRepository expendsRepository;

    public UserBudgetService(
            UserBudgetRepository userBudgetRepository,
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            ExpendsRepository expendsRepository) {
        this.userBudgetRepository = userBudgetRepository;
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.expendsRepository = expendsRepository;
    }

    public List<UserBudgetResponseVo> getUserBudgetByUserId(Long userId) {
        return userBudgetRepository.findByUserId_UserId(userId).stream()
                .map(this::toResponseVo)
                .toList();
    }

    public UserBudgetResponseVo getUserBudgetById(Long userBudgetId) {
        UserBudget ub = userBudgetRepository.findByUserBudgetId(userBudgetId);
        if (ub == null) {
            throw new IllegalArgumentException("UserBudget not found: " + userBudgetId);
        }
        return toResponseVo(ub);
    }

    public List<UserBudgetResponseVo> getUserBudgetByBudgetId(Long budgetId) {
        return userBudgetRepository.findByBudgetId_BudgetId(budgetId).stream()
                .map(this::toResponseVo)
                .toList();
    }

    public UserBudget createUserBudget(UserBudgetVo userBudget) {
        User user = null;
        if (userBudget.getPhoneNumber() != null) {
            user = userRepository.findByPhoneNumber(userBudget.getPhoneNumber());
            if (user == null) {
                throw new IllegalArgumentException("User not found: " + userBudget.getPhoneNumber());
            }
        }
        Budget budget = null;
        if (userBudget.getBudgetId() != null) {
            budget = budgetRepository
                    .findById(userBudget.getBudgetId())
                    .orElseThrow(() -> new IllegalArgumentException("Budget not found: " + userBudget.getBudgetId()));
        }

        UserBudget newUserBudget =
                UserBudget.builder().userId(user).budgetId(budget).build();

        return userBudgetRepository.save(newUserBudget);
    }

    public void deleteUserBudget(Long userBudgetId) {
        UserBudget ub = userBudgetRepository.findByUserBudgetId(userBudgetId);

        if (ub == null) {
            throw new IllegalArgumentException("UserBudget not found: " + userBudgetId);
        }

        List<Expends> e = expendsRepository.findByUser_UserIdAndBudget_BudgetId(
                ub.getUserId().getUserId(), ub.getBudgetId().getBudgetId());

        if (!e.isEmpty()) {
            expendsRepository.deleteAll(e);
        }

        userBudgetRepository.delete(ub);
    }

    public void deleteUserPhoneBudget(UserBudgetVo userBudget) {
        User user = userRepository.findByPhoneNumber(userBudget.getPhoneNumber());

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userBudget.getPhoneNumber());
        }

        UserBudget ub = userBudgetRepository.findByUserId_UserIdAndBudgetId_BudgetId(
                user.getUserId(), userBudget.getBudgetId());

        if (ub == null) {
            throw new IllegalArgumentException("User expends link not found: " + userBudget.getPhoneNumber());
        }

        List<Expends> e = expendsRepository.findByUser_UserIdAndBudget_BudgetId(
                ub.getUserId().getUserId(), ub.getBudgetId().getBudgetId());

        if (!e.isEmpty()) {
            expendsRepository.deleteAll(e);
        }

        userBudgetRepository.delete(ub);
    }

    private UserBudgetResponseVo toResponseVo(UserBudget ub) {
        return UserBudgetResponseVo.builder()
                .userBudgetId(ub.getUserBudgetId())
                .user(UserVo.builder()
                        .userId(ub.getUserId().getUserId())
                        .userName(ub.getUserId().getUserName())
                        .email(ub.getUserId().getEmail())
                        .phoneNumber(ub.getUserId().getPhoneNumber())
                        .build())
                .budget(BudgetSimpleVo.builder()
                        .budgetId(ub.getBudgetId().getBudgetId())
                        .budgetName(ub.getBudgetId().getBudgetName())
                        .commentary(ub.getBudgetId().getCommentary())
                        .build())
                .build();
    }
}
