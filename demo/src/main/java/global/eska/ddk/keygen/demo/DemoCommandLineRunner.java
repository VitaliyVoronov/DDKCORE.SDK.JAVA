package global.eska.ddk.keygen.demo;

import global.eska.ddk.api.client.model.*;
import global.eska.ddk.api.client.service.DDKService;
import global.eska.ddk.keygen.account.AccountCreator;
import global.eska.ddk.keygen.passphrase.PassphraseGenerator;
import global.eska.ddk.keygen.sodium.KeyPair;
import global.eska.ddk.keygen.sodium.KeyPairCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class DemoCommandLineRunner implements CommandLineRunner {

    //    private final CountDownLatch doneSignal;
//    private final Middleware middleware;

    @Value("${greetings-phrase}")
    private String greetingsPhrase;

    private final DDKService ddkService;

    private final PassphraseGenerator passphraseGenerator;

    private final KeyPairCreator keyPairCreator;

    private final AccountCreator accountCreator;

    @Autowired
    public DemoCommandLineRunner(DDKService ddkService, PassphraseGenerator passphraseGenerator, KeyPairCreator keyPairCreator, AccountCreator accountCreator) {
        this.ddkService = ddkService;
        this.passphraseGenerator = passphraseGenerator;
        this.keyPairCreator = keyPairCreator;
        this.accountCreator = accountCreator;
    }

    @Override
    public void run(String... args) {
        log.info("{}", greetingsPhrase);

        String passphrase = passphraseGenerator.createPassphrase();
        log.info("Random passphrase generated successful: {}", passphrase);

        KeyPair keyPair = keyPairCreator.createKeyPair(passphrase);

        log.info("KeyPair generated successful:");
        log.info("public key: {}", keyPair.getPublicKey());
        log.info("public key hex: {}", keyPair.getPublicKeyHex());
        log.info("secret key: {}", keyPair.getSecretKey());
        log.info("secret key hex: {}", keyPair.getSecretKeyHex());

        BigDecimal address = accountCreator.getAddressByPublicKey(keyPair.getPublicKey());

        log.info("DDK address generated successful: {}", address);

        Filter filter = new Filter(null, "cbb9449abb9672d33fa2eb200b1c8b03db7c6572dfb6e59dc334c0ab82b63ab0", null);
        Sort sort = new Sort("createdAt", SortDirection.ASC);

        for (int i = 0; i < 50; i++) {
            getAccount();
            getAccountBalance();
            getTransaction();
            getTransactions(filter, 10, 0, sort);
            System.out.println("Exequted!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + i + " times!");
        }
    }

    private void getTransactions(Filter filter, int limit, int offset, Sort... sorts) {
        List<Transaction> transactions = ddkService.getTransactions(filter, limit, offset, sorts);
        System.out.println("TRANSACTIONS: " + transactions);
    }

    private void getAccount() {
        Account account = ddkService.getAccount("4995063339468361088");
        System.out.println("ACCOUNT: " + account);
    }

    private void getAccountBalance() {
        Long balance = ddkService.getAccountBalance("4995063339468361088");
        System.out.println("BALANCE: " + balance);
    }

    private void getTransaction() {
        System.out.println("TRANSACTION: " + ddkService.getTransaction("c7d80bf1bb220e62735bd388549a87c0cd93b8be30a1ae2f7291ce20d2a94b79"));
    }
}
