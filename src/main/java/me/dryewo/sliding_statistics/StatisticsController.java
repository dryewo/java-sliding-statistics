package me.dryewo.sliding_statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
public class StatisticsController {
    private Logger log = LoggerFactory.getLogger(StatisticsController.class);

    static long WINDOW_SIZE_MS = 60 * 1000;
    static long REFRESH_INTERVAL_MS = 1000;

    StatisticsAsync statisticsAsync = new StatisticsAsync(WINDOW_SIZE_MS, REFRESH_INTERVAL_MS);

    @GetMapping("/statistics")
    public @ResponseBody
    Result getStatistics() {
        return statisticsAsync.get();
    }

    @PostMapping("/transactions")
    public ResponseEntity postTransactions(@Valid @RequestBody Transaction transaction) {
        long now = System.currentTimeMillis();
        if (transaction.timestamp + WINDOW_SIZE_MS < now) {
            log.info("Ignored transaction {} because it's too old", transaction);
            return ResponseEntity.status(204).build();
        } else {
            log.info("Accepted transaction {}", transaction);
            statisticsAsync.add(transaction.createItem());
            return ResponseEntity.status(201).build();
        }
    }

    static class Transaction {
        @NotNull
        Long timestamp;
        @NotNull
        Double amount;

        Item createItem() {
            return new Item(timestamp, amount);
        }

        @Override
        public String toString() {
            return "Transaction{timestamp=" + timestamp + ", amount=" + amount + '}';
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

}
