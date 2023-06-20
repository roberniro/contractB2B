package civilCapstone.contractB2B.contractor.controller;

import civilCapstone.contractB2B.contractor.model.ExperienceDto;
import civilCapstone.contractB2B.contractor.service.ContractorExperienceService;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.global.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("contractor")
// 하청업체 경력 관련 기능 컨트롤러
public class ContractorExperienceController {
    @Autowired
    private ContractorExperienceService contractorExperienceService;
    @Autowired
    private Validator validator;

    // 경력 조회 요청 처리
    @GetMapping("/experience")
    public ResponseEntity getExperience(@AuthenticationPrincipal String username) {
        return contractorExperienceService.getExperience(username);
    }

    // 경력 생성 요청 처리
    @PostMapping("/experience")
    public ResponseEntity createExperience(@AuthenticationPrincipal String username, @Valid @RequestBody ExperienceDto experienceDto, Errors errors) {
        // 경력 생성 요청 유효성 검사
        if (errors.hasErrors()) {
            ResponseDto responseErrorDto = ResponseDto.builder()
                    .error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        return contractorExperienceService.createExperience(username, experienceDto);
    }
}
