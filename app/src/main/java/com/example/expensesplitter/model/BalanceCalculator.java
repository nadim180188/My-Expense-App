package com.example.expensesplitter.model;

import com.example.expensesplitter.data.entity.*;
import java.util.*;

public class BalanceCalculator {

    public static List<MemberBalance> calculate(
            List<MemberEntity> members,
            List<ExpenseShareEntity> shares,
            List<ExpenseEntity> expenses,
            List<SettlementEntity> settlements) {

        List<MemberBalance> result = new ArrayList<>();
        for (MemberEntity m : members) {
            double share = 0, paid = 0, settled = 0;

            for (ExpenseShareEntity s : shares)
                if (s.memberId == m.id) share += s.shareAmount;

            for (ExpenseEntity e : expenses)
                if (e.paidByMemberId == m.id) paid += e.amount;

            for (SettlementEntity st : settlements) {
                if (st.memberId != m.id) continue;
                if ("PAID".equals(st.type)) settled += st.amount;
                else settled -= st.amount;
            }

            result.add(new MemberBalance(m.id, m.name, share, paid, settled));
        }
        return result;
    }
}
