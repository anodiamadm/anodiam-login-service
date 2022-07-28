package com.anodiam.login.models;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AnodiamRoleConverter implements Converter<String, AnodiamRole> {
    @Override
    public AnodiamRole convert(String value) {
        return AnodiamRole.valueOf(value.toUpperCase());
    }
}
