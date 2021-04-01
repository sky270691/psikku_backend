package com.psikku.backend.mapper.test;

import com.psikku.backend.dto.test.MinimalTestDto;
import com.psikku.backend.entity.Subtest;
import com.psikku.backend.entity.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {
    private final Logger logger = LoggerFactory.getLogger(TestMapper.class);

    public MinimalTestDto convertToMinTestDto(Test test){
        MinimalTestDto minimalTestDto = new MinimalTestDto();
        minimalTestDto.setName(test.getName());
        minimalTestDto.setId(test.getId());
        minimalTestDto.setView(test.isView());
        minimalTestDto.setInternalName(test.getInternalName());
        minimalTestDto.setDescription(test.getDescription());
        int duration = test.getSubtestList().stream()
                .mapToInt(Subtest::getDuration)
                .sum();
        minimalTestDto.setDuration(duration);
        minimalTestDto.setSkippable(test.getSkippable());
        return minimalTestDto;
    }
}
