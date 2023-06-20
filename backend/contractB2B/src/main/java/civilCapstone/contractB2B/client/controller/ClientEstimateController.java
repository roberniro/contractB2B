package civilCapstone.contractB2B.client.controller;

import civilCapstone.contractB2B.client.service.ClientEstimateStatusService;
import civilCapstone.contractB2B.contractor.model.ReasonDto;
import civilCapstone.contractB2B.estimate.model.EstimateDto;
import civilCapstone.contractB2B.client.service.ClientEstimateService;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.global.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("client")
// 원청 견적 관련 기능 컨트롤러
public class ClientEstimateController {
    @Autowired
    private ClientEstimateService clientEstimateService;
    @Autowired
    private ClientEstimateStatusService clientEstimateStatusService;
    @Autowired
    private Validator validator;

    // 하청업체 조회 요청 처리
    @GetMapping("/contractor")
    public ResponseEntity getContractor(@AuthenticationPrincipal String username) {
        return clientEstimateService.getContractor(username);
    }

    // 견적 발송 요청 처리
    @PostMapping("/estimate")
    public ResponseEntity createEstimate(@AuthenticationPrincipal String username, @Valid @RequestBody EstimateDto.EstimateRequestDto estimateDto, Errors errors) {
        // 견적 요청 유효성 검사
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return clientEstimateService.createEstimate(username, estimateDto);
    }

    // 재견적 발송 요청 처리
    @PostMapping("/estimate/{motherId}")
    public ResponseEntity createChildEstimate(@AuthenticationPrincipal String username, @PathVariable String motherId,
                                              @Valid @RequestBody EstimateDto.ClientChildEstimateRequestDto estimateDto, Errors errors) {
        // 재견적 요청 유효성 검사
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return clientEstimateService.createChildEstimate(username, motherId, estimateDto);
    }

    // 견적 조회 요청 처리
    @GetMapping("/estimate")
    public ResponseEntity getEstimate(@AuthenticationPrincipal String username) {
        return clientEstimateService.getEstimate(username);
    }

    // 원견적 조회 요청 처리
    @GetMapping("/estimate/mother")
    public ResponseEntity getMotherEstimate(@AuthenticationPrincipal String username) {
        return clientEstimateService.getMotherEstimate(username);
    }

    // 재견적 조회 요청 처리
    @GetMapping("/estimate/{motherId}")
    public ResponseEntity getChildEstimate(@AuthenticationPrincipal String username, @PathVariable String motherId) {
        return clientEstimateService.getChildEstimate(username, motherId);
    }

    // 견적 수락 요청 처리
    @PostMapping("/estimate/{estimateId}/accept")
    public ResponseEntity acceptEstimate(@AuthenticationPrincipal String username, @PathVariable String estimateId,
                                         @Valid @RequestBody ReasonDto reasonDto, Errors errors) {
        // 견적 수락 요청 유효성 검사
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return clientEstimateStatusService.acceptEstimate(username, estimateId, reasonDto);
    }

    // 견적 삭제 요청 처리
    @DeleteMapping("/estimate/{estimateId}")
    public ResponseEntity deleteEstimate(@AuthenticationPrincipal String username, @PathVariable String estimateId) {
        return clientEstimateStatusService.deleteEstimate(username, estimateId);
    }
}
