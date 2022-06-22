package com.stardata.starshop2.sharedcontext.south.adapter;

import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.BizParameter;
import com.stardata.starshop2.sharedcontext.domain.StringIdentity;
import com.stardata.starshop2.sharedcontext.south.port.BizParameterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:24
 */
@Adapter(PortType.Repository)
@Component
@AllArgsConstructor
public class BizParameterRepositoryJpaAdapter implements BizParameterRepository {
    private final GenericRepository<BizParameter, StringIdentity> paramRepository;

    @Override
    public BizParameter instanceOf(String key) {
        return paramRepository.findById(StringIdentity.from(key));
    }
}
