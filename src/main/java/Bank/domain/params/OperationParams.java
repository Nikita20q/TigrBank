package Bank.domain.params;

import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OperationParams(
        UUID id,
        UUID bankAccountId,
        UUID cattegoryId ,
        FlowDirection flowDirection,
        BigDecimal amount,
        LocalDateTime date,
        String description
) implements EntityParams {}
