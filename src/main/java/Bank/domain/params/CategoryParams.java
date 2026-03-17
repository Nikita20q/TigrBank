package Bank.domain.params;

import Bank.domain.enums.FlowDirection;

import java.util.UUID;

public record CategoryParams(UUID id, String categoryName, FlowDirection flowDirection) implements EntityParams {}
