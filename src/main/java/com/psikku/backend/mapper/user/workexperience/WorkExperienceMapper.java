package com.psikku.backend.mapper.user.workexperience;

import com.psikku.backend.dto.user.detail.WorkExperienceDto;
import com.psikku.backend.entity.WorkExperience;
import org.springframework.stereotype.Component;

@Component
public class WorkExperienceMapper {

    public WorkExperience convertDtoToWorkExperienceEntity(WorkExperienceDto dto){

        WorkExperience workExperience = new WorkExperience();

        if(dto.getCompanyName() != null && !dto.getCompanyName().equalsIgnoreCase("")){
            workExperience.setCompanyName(dto.getCompanyName());
        }
        if(dto.getJobDesc() != null && !dto.getJobDesc().equalsIgnoreCase("")){
            workExperience.setJobDesc(dto.getJobDesc());
        }
        if(dto.getId() != null && dto.getId() != 0){
            workExperience.setId(dto.getId());
        }
        if(dto.getStart() != null){
            workExperience.setStart(dto.getStart());
        }
        if(dto.getEnd() != null){
            workExperience.setEnd(dto.getEnd());
        }

        return workExperience;
    }

    public WorkExperienceDto convertWorkExperienceEntityToDto(WorkExperience workExperience){

        WorkExperienceDto dto = new WorkExperienceDto();

        if(workExperience.getCompanyName() != null && !workExperience.getCompanyName().equalsIgnoreCase("")){
            dto.setCompanyName(workExperience.getCompanyName());
        }
        if(workExperience.getId() != null && workExperience.getId() != 0){
            dto.setId(workExperience.getId());
        }
        if(workExperience.getJobDesc() != null && !workExperience.getJobDesc().equalsIgnoreCase("")){
            dto.setJobDesc(workExperience.getJobDesc());
        }
        if(workExperience.getStart() != null){
            dto.setStart(workExperience.getStart());
        }
        if(workExperience.getEnd() != null){
            dto.setEnd(workExperience.getEnd());
        }
        return dto;
    }

}
