package org.domainspecific.dsl.main.ruleengine;

public class FXTransaction {
    final private String source;
    final private String target;
    final private double amount;
    final private long transactionDate;
    final private String bankCode;
    final private String accountNum;
    private double discount;

    public FXTransaction(String source, String target, double amount, long transactionDate, String bankCode, String accountNum) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.bankCode = bankCode;
        this.accountNum = accountNum;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getAmount() {
        return amount;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
