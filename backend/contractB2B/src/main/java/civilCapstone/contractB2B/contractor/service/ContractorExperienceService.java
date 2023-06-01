package civilCapstone.contractB2B.contractor.service;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.entity.Experience;
import civilCapstone.contractB2B.contractor.model.ExperienceDto;
import civilCapstone.contractB2B.contractor.repository.ContractorRepository;
import civilCapstone.contractB2B.contractor.repository.ExperienceRepository;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.entity.User;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Service
@Slf4j
public class ContractorExperienceService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private ExperienceRepository experienceRepository;

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public ResponseEntity getExperience(String username) {
        try {
            User user = userRepository.findByUsername(username).get();
            if (!contractorRepository.existsByContractor(user) || user.getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder()
                        .error(Collections.singletonMap("get_experience", "사용자가 하청업체가 아닙니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Contractor contractor = contractorRepository.findByContractor(user);
            if (!experienceRepository.existsByContractor(contractor)) {
                ResponseDto responseErrorDto = ResponseDto.builder()
                        .error(Collections.singletonMap("get_experience", "경력이 존재하지 않습니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            List<Experience> experiences = experienceRepository.findAllByContractor(contractor);
            List<ExperienceDto> experienceDtos = new ArrayList<>();
            for (Experience experience : experiences) {
                experienceDtos.add(ExperienceDto.builder()
                        .id(experience.getId())
                        .name(experience.getName())
                        .contractorId(experience.getContractor().getId().toString())
                        .contractorName(experience.getContractor().getContractor().getName())
                        .clientName(experience.getClientName())
                        .field(experience.getField())
                        .site(experience.getSite())
                        .period(experience.getPeriod())
                        .budget(experience.getBudget())
                        .content(experience.getContent())
                        .build());
            }
            return ResponseEntity.ok().body(Collections.singletonMap("get_experience", experienceDtos));
        } catch (Exception e) {
            log.error("getExperience", e);
            ResponseDto responseErrorDto = ResponseDto.builder()
                    .error(Collections.singletonMap("get_experience", "경력 조회에 실패했습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    public ResponseEntity createExperience(String username, ExperienceDto experienceDto) {
        try {
            if (!userRepository.existsByUsername(username)) {
                ResponseDto responseErrorDto = ResponseDto.builder()
                        .error(Collections.singletonMap("create_experience", "사용자 정보가 존재하지 않습니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            User user = userRepository.findByUsername(username).get();
            if (!contractorRepository.existsByContractor(user) || user.getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder()
                        .error(Collections.singletonMap("create_experience", "사용자가 하청업체가 아닙니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Contractor contractor = contractorRepository.findByContractor(user);
            Experience experience = Experience.builder()
                    .name(experienceDto.getName())
                    .contractor(contractor)
                    .clientName(experienceDto.getClientName())
                    .field(experienceDto.getField())
                    .site(experienceDto.getSite())
                    .period(experienceDto.getPeriod())
                    .budget(experienceDto.getBudget())
                    .content(experienceDto.getContent())
                    .build();
            experienceRepository.save(experience);
            ExperienceDto experienceResponseDto = ExperienceDto.builder()
                    .id(experience.getId())
                    .name(experience.getName())
                    .contractorId(experience.getContractor().getId().toString())
                    .contractorName(experience.getContractor().getContractor().getName())
                    .field(experience.getField())
                    .site(experience.getSite())
                    .period(experience.getPeriod())
                    .budget(experience.getBudget())
                    .content(experience.getContent())
                    .build();
            return ResponseEntity.ok().body(Collections.singletonMap("create_experience", experienceResponseDto));
        } catch (Exception e) {
            log.error("createExperience", e);
            ResponseDto responseErrorDto = ResponseDto.builder()
                    .error(Collections.singletonMap("create_experience", "경력 등록에 실패했습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }
}
