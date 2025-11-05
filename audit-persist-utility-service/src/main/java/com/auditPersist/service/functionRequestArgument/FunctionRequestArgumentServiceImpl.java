package com.auditPersist.service.functionRequestArgument;

import com.auditPersist.entity.FunctionRequest;
import com.auditPersist.entity.FunctionRequestArgument;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.repository.FunctionRequestArgumentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionRequestArgumentServiceImpl  extends GenericServiceImpl<FunctionRequestArgument, FunctionRequestArgumentRepository, String> implements FunctionRequestArgumentService {

    public FunctionRequestArgumentServiceImpl(FunctionRequestArgumentRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public List<FunctionRequestArgument> saveFromFunctionDescription(String[] argumentType, List<String> argumentData, FunctionRequest functionRequest) {
        if(argumentType.length != argumentData.size()) {
            //TODO : ADD EXCEPTION TO AUDIT
            return null;
        }
        List<FunctionRequestArgument> functionRequestArguments = new ArrayList<>();
        for (int i = 0 ; i< argumentType.length;i++){
            FunctionRequestArgument functionRequestArgument =
                    FunctionRequestArgument.builder()
                            .type(argumentType[i])
                            .content(argumentData.get(i))
                            .functionRequest(functionRequest)
                            .build();
            functionRequestArguments.add(functionRequestArgument);
        }
        return this.saveAll(functionRequestArguments);
    }
}

