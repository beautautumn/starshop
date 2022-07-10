package com.stardata.starshop2.authcontext.south.adapter;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.WxOpenId;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:17
 */
@Adapter(PortType.Repository)
@Component
@AllArgsConstructor
public class UserRepositoryJpaAdapter implements UserRepository {
    private final GenericRepository<User, LongIdentity> repositoryInstance;

    @Override
    public User findByOpenId(WxOpenId openID) {
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("openid"), openID);
        List<User> users = repositoryInstance.findBy(specification);
        return users.size()>0? users.get(0): null;
    }

    @Override
    public void add(User user) {
        repositoryInstance.saveOrUpdate(user);
    }

    @Override
    public void update(User user) {
        repositoryInstance.saveOrUpdate(user);
    }

    @Override
    public User instanceOf(LongIdentity userId) {
        return repositoryInstance.findById(userId);
    }
}
