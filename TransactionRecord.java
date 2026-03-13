import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TransactionRecord {
    private final String time;
    private final String atmName;
    private final String status;
    private final int amount;
    private final int remainingBalance;

    public TransactionRecord(String atmName, String status, int amount, int remainingBalance) {
        this.time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.atmName = atmName;
        this.status = status;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
    }

    public Object[] toRow() {
        return new Object[] { time, atmName, status, "$" + amount, "$" + remainingBalance };
    }
}
