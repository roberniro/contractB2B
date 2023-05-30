package civilCapstone.contractB2B.client.service;

import civilCapstone.contractB2B.global.entity.Construction;
import civilCapstone.contractB2B.global.entity.Estimate;
import civilCapstone.contractB2B.global.model.EstimateDto;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.global.repository.ConstructionRepository;
import civilCapstone.contractB2B.global.repository.EstimateRepository;
import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static civilCapstone.contractB2B.global.entity.EstimateStatus.ACCEPTED;

@Slf4j
@Service
public class ClientEstimateStatusService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private ConstructionRepository constructionRepository;

    public ResponseEntity acceptEstimate(String username, Long estimateId, EstimateDto.EstimateRequestDto estimateRequestDto) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!estimateRepository.existsById(estimateId)) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서가 존재하지 않습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        Estimate estimate = estimateRepository.findById(estimateId).get();
        if (estimate.getClient().getUsername() != username) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서의 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        estimate.setEstimateStatus(ACCEPTED);
        estimateRepository.save(estimate);
        Construction construction = estimateToConstruction(estimate, estimateRequestDto);
        constructionRepository.save(construction);
        EstimateDto estimateStatusResponseDto = getEstimateStatusDto(estimate);
        return ResponseEntity.ok().body(estimateStatusResponseDto);
    }

    public ResponseEntity deleteEstimate(String username, Long estimateId) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!estimateRepository.existsById(estimateId)) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서가 존재하지 않습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        Estimate estimate = estimateRepository.findById(estimateId).get();
        if (estimate.getClient().getUsername() != username) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서의 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        estimateRepository.deleteById(estimateId);
        return ResponseEntity.ok().body(Collections.singletonMap("delete_estimate", "견적서가 삭제되었습니다"));
    }

    private EstimateDto getEstimateStatusDto(Estimate estimate) {
        EstimateDto estimateResponseDto = EstimateDto.builder()
                .id(estimate.getId())
                .clientId(estimate.getClient().getId().toString())
                .clientName(estimate.getClient().getName())
                .contractorId(estimate.getContractor().getId().toString())
                .contractorName(estimate.getContractor().getName())
                .field(estimate.getField())
                .city(estimate.getSite().getCity())
                .district(estimate.getSite().getDistrict())
                .addressDetail(estimate.getSite().getAddressDetail())
                .period(estimate.getPeriod())
                .budget(estimate.getBudget())
                .clientContent(estimate.getClientContent())
                .contractorContent(estimate.getContractorContent())
                .estimateStatus(estimate.getEstimateStatus())
                .build();
        return estimateResponseDto;
    }

    private Construction estimateToConstruction(Estimate estimate, EstimateDto.EstimateRequestDto estimateRequestDto) {
        Construction construction = Construction.builder()
                .client(estimate.getClient())
                .contractor(estimate.getContractor())
                .field(estimate.getField())
                .site(estimate.getSite())
                .period(estimate.getPeriod())
                .budget(estimate.getBudget())
                .contractContent(estimate.getClientContent())
                .reason(estimateRequestDto.getReason())
                .build();
        return construction;
    }
}