import java.util.concurrent.Semaphore;

public class BankAccount {
    private int balance;
    private final Semaphore semaphore = new Semaphore(1);
    private final TransactionListener listener;

    // Interfaz para comunicar eventos a la GUI sin acoplar el código
    public interface TransactionListener {
        void onTransactionProcessed(TransactionRecord record, boolean isSuccess);
    }

    public BankAccount(int initialBalance, TransactionListener listener) {
        this.balance = initialBalance;
        this.listener = listener;
    }

    public boolean withdraw(int amount, String threadName) {
        boolean success = false;
        TransactionRecord record = null;

        try {
            // Adquirir el semáforo: Inicio de Sección Crítica
            semaphore.acquire();

            if (balance >= amount) {
                balance -= amount;
                record = new TransactionRecord(threadName, "Aprobado", amount, balance);
                success = true;
            } else {
                record = new TransactionRecord(threadName, "Rechazado", amount, balance);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Liberar el semáforo: Fin de Sección Crítica
            semaphore.release();
        }

        // Notificar a la GUI fuera de la sección crítica para no bloquear el semáforo
        if (listener != null && record != null) {
            listener.onTransactionProcessed(record, success);
        }

        return success;
    }

    public int getBalance() {
        return balance;
    }
}