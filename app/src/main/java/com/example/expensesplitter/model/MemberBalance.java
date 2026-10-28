package com.example.expensesplitter.model;

public class MemberBalance {
    public long memberId;
    public String name;
    public double totalShare;
    public double totalPaidAsPayer;
    public double totalSettled;
    public double remaining;

    public MemberBalance(long memberId, String name, double share, double paid, double settled) {
        this.memberId = memberId;
        this.name = name;
        this.totalShare = share;
        this.totalPaidAsPayer = paid;
        this.totalSettled = settled;
        this.remaining = share - settled;   // ★ এখানেই আপনার কাঙ্ক্ষিত লজিক
    }
}
