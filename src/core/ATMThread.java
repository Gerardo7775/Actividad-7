package core;

import java.util.Random;
import models.BankAccount;

public class ATMThread extends Thread {
    private final BankAccount account;
    private final int maxIterations;
    private final Random random = new Random();
    private final Runnable onComplete;

    public ATMThread(BankAccount account, String name, int maxIterations, Runnable onComplete) {
        super(name);
        this.account = account;
        this.maxIterations = maxIterations;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        for (int i = 0; i < maxIterations; i++) {
            // Cantidad aleatoria entre 50 y 300
            int amount = random.nextInt(251) + 50;
            account.withdraw(amount, getName());

            try {
                // Simula el tiempo de red/procesamiento del cliente
                Thread.sleep(random.nextInt(600) + 200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        // Avisar que este hilo terminó
        if (onComplete != null) {
            onComplete.run();
        }
    }
}
