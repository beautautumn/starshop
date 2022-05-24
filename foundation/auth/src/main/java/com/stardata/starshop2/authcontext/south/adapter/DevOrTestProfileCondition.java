package com.stardata.starshop2.authcontext.south.adapter;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Profiles;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/24 15:16
 */
public class DevOrTestProfileCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        Profiles profiles = Profiles.of("dev", "test");
        return context.getEnvironment().acceptsProfiles(profiles);
    }
}
