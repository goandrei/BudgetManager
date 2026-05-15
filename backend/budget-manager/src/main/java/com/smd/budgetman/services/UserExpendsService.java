package com.smd.budgetman.services;

import com.smd.budgetman.vo.UserVos.UserExpendsResponseVo;
import com.smd.budgetman.vo.UserVos.UserExpendsVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserExpendsService {

    public List<UserExpendsResponseVo> getUserExpendsByUserId(Long userId) {
        return null;
    }

    public List<UserExpendsResponseVo> getUserExpendsByExpendsId(Long expendsId) {
        return null;
    }

    public UserExpendsResponseVo getUserExpendsById(Long userExpendsId) {
        return null;
    }

    public void createUserExpends(UserExpendsVo userExpends) {}

    public void deleteUserExpends(Long userExpendsId) {}
}
