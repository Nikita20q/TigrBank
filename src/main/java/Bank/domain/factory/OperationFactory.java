package Bank.domain.factory;

import Bank.domain.model.Operation;
import Bank.domain.params.OperationParams;
import org.springframework.stereotype.Service;

@Service
public class OperationFactory implements EntityFactory<Operation, OperationParams> {

    @Override
    public Operation createWithParams(OperationParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }
        return new Operation
                (
                    params.id(),
                    params.bankAccountId(),
                    params.cattegoryId(),
                    params.flowDirection(),
                    params.amount(),
                    params.date(),
                    params.description()
                );
    }
}
