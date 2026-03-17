package Bank.domain.params;

import java.math.BigDecimal;
import java.util.UUID;

public record BankAccountParams(UUID id, String name, BigDecimal balance) implements EntityParams {}
