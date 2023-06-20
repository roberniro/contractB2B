package civilCapstone.contractB2B.contractor.controller;

import civilCapstone.contractB2B.contractor.service.ContractorEstimateService;
import civilCapstone.contractB2B.estimate.model.EstimateDto;
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
// 하청업체 견적 관련 기능 컨트롤러
public class ContractorEstimateController {
    @Autowired
    private ContractorEstimateService contractorEstimateService;

    @Autowired
    private Validator validator;

    // 견적 조회 요청 처리
    @GetMapping("/estimate")
    public ResponseEntity getEstimate(@AuthenticationPrincipal String username) {
        return contractorEstimateService.getEstimate(username);
    }

    // 원견적 조회 요청 처리
    @GetMapping("/estimate/mother")
    public ResponseEntity getMotherEstimate(@AuthenticationPrincipal String username) {
        return contractorEstimateService.getMotherEstimate(username);
    }

    // 재견적 조회 요청 처리
    @GetMapping("/estimate/{motherId}")
    public ResponseEntity getChildEstimate(@AuthenticationPrincipal String username, @PathVariable String motherId) {
        return contractorEstimateService.getChildEstimate(username, motherId);
    }

    // 견적 수락 요청 처리
    @PutMapping("/estimate/{estimateId}/accept")
    public ResponseEntity acceptEstimate(@AuthenticationPrincipal String username, @PathVariable String estimateId) {
        return contractorEstimateService.acceptEstimate(username, estimateId);
    }

    // 견적 거절 요청 처리
    @PutMapping("/estimate/{estimateId}/reject")
    public ResponseEntity rejectEstimate(@AuthenticationPrincipal String username, @PathVariable String estimateId) {
        return contractorEstimateService.rejectEstimate(username, estimateId);
    }

    // 재견적 생성 요청 처리
    @PostMapping("/estimate/{motherId}")
    public ResponseEntity createChildEstimate(@AuthenticationPrincipal String username, @PathVariable String motherId,
                                              @Valid  @RequestBody EstimateDto.ContractorChildEstimateRequestDto estimateDto, Errors errors) {
        // 재견적 요청 유효성 검사
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return contractorEstimateService.createChildEstimate(username, motherId, estimateDto);
    }
}
