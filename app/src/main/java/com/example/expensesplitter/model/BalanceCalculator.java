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

            for (ExpenseShareEntity s : shares) {
                // s.memberId == 0 মানে ডামি আইডি, সেটাও এই মেম্বারের সাথে যোগ হবে
                if (s.memberId == m.id || s.memberId == 0) share += s.shareAmount;
            }

            for (ExpenseEntity e : expenses)
                if (e.paidByMemberId == m.id || e.paidByMemberId == 0) paid += e.amount;

            for (SettlementEntity st : settlements) {
                if (st.memberId != m.id && st.memberId != 0) continue;
                if ("PAID".equals(st.type)) settled += st.amount;
                else settled -= st.amount;
            }

            result.add(new MemberBalance(m.id, m.name, share, paid, settled));
        }
        return result;
    }
}
