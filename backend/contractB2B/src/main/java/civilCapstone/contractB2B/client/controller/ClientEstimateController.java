package civilCapstone.contractB2B.client.controller;

import civilCapstone.contractB2B.client.service.ClientEstimateStatusService;
import civilCapstone.contractB2B.contractor.model.ReasonDto;
import civilCapstone.contractB2B.global.model.EstimateDto;
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
public class ClientEstimateController {
    @Autowired
    private ClientEstimateService clientEstimateService;
    @Autowired
    private ClientEstimateStatusService clientEstimateStatusService;
    @Autowired
    private Validator validator;

    @GetMapping("/contractor")
    public ResponseEntity getContractor(@AuthenticationPrincipal String username) {
        return clientEstimateService.getContractor(username);
    }

    @PostMapping("/estimate")
    public ResponseEntity createEstimate(@AuthenticationPrincipal String username, @Valid @RequestBody EstimateDto.EstimateRequestDto estimateDto, Errors errors) {
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return clientEstimateService.createEstimate(username, estimateDto);
    }

    @PostMapping("/estimate/{motherId}")
    public ResponseEntity createChildEstimate(@AuthenticationPrincipal String username, @PathVariable String motherId,
                                              @Valid @RequestBody EstimateDto.ChildEstimateRequestDto estimateDto, Errors errors) {
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return clientEstimateService.createChildEstimate(username, motherId, estimateDto);
    }

    @GetMapping("/estimate")
    public ResponseEntity getEstimate(@AuthenticationPrincipal String username) {
        return clientEstimateService.getEstimate(username);
    }

    @GetMapping("/estimate/mother")
    public ResponseEntity getMotherEstimate(@AuthenticationPrincipal String username) {
        return clientEstimateService.getMotherEstimate(username);
    }

    @GetMapping("/estimate/{motherId}")
    public ResponseEntity getChildEstimate(@AuthenticationPrincipal String username, @PathVariable String motherId) {
        return clientEstimateService.getChildEstimate(username, motherId);
    }

    @PostMapping("/estimate/{estimateId}/accept")
    public ResponseEntity acceptEstimate(@AuthenticationPrincipal String username, @PathVariable String estimateId,
                                         @Valid @RequestBody ReasonDto reasonDto, Errors errors) {
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return clientEstimateStatusService.acceptEstimate(username, estimateId, reasonDto);
    }

    @DeleteMapping("/estimate/{estimateId}")
    public ResponseEntity deleteEstimate(@AuthenticationPrincipal String username, @PathVariable String estimateId) {
        return clientEstimateStatusService.deleteEstimate(username, estimateId);
    }
}
