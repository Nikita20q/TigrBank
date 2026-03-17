package Bank.domain.factory;

import Bank.domain.model.BankAccount;
import Bank.domain.params.BankAccountParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountFactory implements EntityFactory<BankAccount, BankAccountParams> {
    @Override
    public BankAccount createWithParams(BankAccountParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }
        return new BankAccount(params.id(), params.name(), params.balance());
    }
}
