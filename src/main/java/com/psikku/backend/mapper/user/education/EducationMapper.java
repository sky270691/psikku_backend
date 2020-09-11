package com.psikku.backend.mapper.user.education;

import com.psikku.backend.dto.user.education.EducationDto;
import com.psikku.backend.entity.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public Education convertDtoToEducationEntity(EducationDto dto) {

        Education education = new Education();
        if(dto.getId() != null){
            education.setId(dto.getId());
        }
        if(dto.getEducationLevel() != null && !dto.getEducationLevel().equalsIgnoreCase("")){
            education.setEducationLevel(dto.getEducationLevel());
        }
        if(dto.getGraduatedYear() != null && dto.getGraduatedYear() != 0){
            education.setGraduatedYear(dto.getGraduatedYear());
        }
        if(dto.getInstitutionName() != null && !dto.getInstitutionName().equalsIgnoreCase("")){
            education.setInstitutionName(dto.getInstitutionName());
        }
        if(dto.getMajor() != null && !dto.getMajor().equalsIgnoreCase("")){
            education.setMajor(dto.getMajor());
        }

        return education;
    }

    public EducationDto convertEntityToEducationDto(Education education){

        EducationDto dto = new EducationDto();

        dto.setId(education.getId());

        if(education.getEducationLevel()!=null && !education.getEducationLevel().equalsIgnoreCase("")){
            dto.setEducationLevel(education.getEducationLevel());
        }
        if(education.getGraduatedYear() != null && education.getGraduatedYear() != 0) {
            dto.setGraduatedYear(education.getGraduatedYear());
        }
        if(education.getInstitutionName() != null && !education.getInstitutionName().equalsIgnoreCase("")) {
            dto.setInstitutionName(education.getInstitutionName());
        }
        if(education.getMajor() != null && !education.getMajor().equalsIgnoreCase("")) {
            dto.setMajor(education.getMajor());
        }

        return dto;
    }


}
