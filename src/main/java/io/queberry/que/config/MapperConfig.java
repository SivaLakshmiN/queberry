package io.queberry.que.config;

import io.queberry.que.mapper.BranchMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapperConfig {

    @Bean
    public BranchMapper branchMapper() {
        return Mappers.getMapper(BranchMapper.class);
    }
}
