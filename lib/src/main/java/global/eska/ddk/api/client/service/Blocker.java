package global.eska.ddk.api.client.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Blocker {

    private CountDownLatch doneSignal;

    public Blocker() {
        this.doneSignal = new CountDownLatch(1);
    }

    public void lock() {
        if (doneSignal.getCount() == 0){
            generate();
        }
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unlock() {
        doneSignal.countDown();
    }

    public CountDownLatch getDoneSignal() {
        return doneSignal;
    }

    public void generate() {
        doneSignal = new CountDownLatch(1);
    }
}
