public class BankStarvation {
    private double balance;
    int id;
 
    BankStarvation(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }
     
    synchronized double getBalance() {
        // Wait to simulate io like database access ...
        try {Thread.sleep(100l);} catch (InterruptedException e) {}
        return balance;
    }
     
    synchronized void withdraw(double amount) {
        balance -= amount;
    }
 
    synchronized void deposit(double amount) {
        balance += amount;
    }
 
    synchronized void transfer(BankStarvation to, double amount) {
            withdraw(amount);
            to.deposit(amount);
    }
 
    public static void main(String[] args) {
        final BankStarvation fooAccount = new BankStarvation(1, 500d);
        final BankStarvation barAccount = new BankStarvation(2, 500d);
         
        Thread balanceMonitorThread1 = new Thread(new BalanceMonitor(fooAccount), "BalanceMonitor");
        Thread transactionThread1 = new Thread(new Transaction(fooAccount, barAccount, 250d), "Transaction-1");
        Thread transactionThread2 = new Thread(new Transaction(fooAccount, barAccount, 250d), "Transaction-2");
         
        balanceMonitorThread1.setPriority(Thread.MAX_PRIORITY);
        transactionThread1.setPriority(Thread.MIN_PRIORITY);
        transactionThread2.setPriority(Thread.MIN_PRIORITY);
         
        // Start the monitor
        balanceMonitorThread1.start();
         
        // And later, transaction threads tries to execute.
        try {Thread.sleep(100l);} catch (InterruptedException e) {}
        transactionThread1.start();
        transactionThread2.start();
 
    }
 
}
class BalanceMonitor implements Runnable {
    private BankStarvation account;
    BalanceMonitor(BankStarvation account) { this.account = account;}
    boolean alreadyNotified = false;
     
    @Override
    public void run() {
        System.out.format("%s started execution%n", Thread.currentThread().getName());
        while (true) {
            if(account.getBalance() <= 0) {
                // send email, or sms, clouds of smoke ...
                break;
            }
        }
        System.out.format("%s : account has gone too low, email sent, exiting.", Thread.currentThread().getName());
    }
     
}
class Transaction implements Runnable {
    private BankStarvation sourceAccount, destinationAccount;
    private double amount;
 
    Transaction(BankStarvation sourceAccount, BankStarvation destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }
 
    public void run() {
        System.out.format("%s started execution%n", Thread.currentThread().getName());
        sourceAccount.transfer(destinationAccount, amount);
        System.out.printf("%s completed execution%n", Thread.currentThread().getName());
    }
 
}