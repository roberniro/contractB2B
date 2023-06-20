package civilCapstone.contractB2B.client.service;

import civilCapstone.contractB2B.contractor.model.ReasonDto;
import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.construction.entity.Construction;
import civilCapstone.contractB2B.construction.entity.ConstructionStatus;
import civilCapstone.contractB2B.estimate.entity.Estimate;
import civilCapstone.contractB2B.estimate.model.EstimateDto;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.construction.repository.ConstructionRepository;
import civilCapstone.contractB2B.estimate.repository.EstimateRepository;
import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static civilCapstone.contractB2B.estimate.entity.EstimateStatus.ACCEPTED;

@Slf4j
@Service
// 원청 견적 상태 관련 서비스
public class ClientEstimateStatusService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private ConstructionRepository constructionRepository;

    // 원청 견적서 수락 요청 처리
    public ResponseEntity acceptEstimate(String username, String estimateId, ReasonDto reasonDto) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!estimateRepository.existsById(Long.parseLong(estimateId))) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서가 존재하지 않습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        Estimate estimate = estimateRepository.findById(Long.parseLong(estimateId)).get();
        if (!estimate.getClient().getUsername().equals(username)) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서의 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        // 견적을 수락하면 견적서의 상태를 ACCEPTED로 변경하고, 공사를 생성한다.
        estimate.setEstimateStatus(ACCEPTED);
        estimateRepository.save(estimate);
        Construction construction = estimateToConstruction(estimate, reasonDto);
        constructionRepository.save(construction);
        EstimateDto estimateStatusResponseDto = getEstimateStatusDto(estimate);
        return ResponseEntity.ok().body(estimateStatusResponseDto);
    }

    // 원청 견적서 삭제 요청 처리

    public ResponseEntity deleteEstimate(String username, String estimateId) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "사용자가 클라이언트가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (!estimateRepository.existsById(Long.parseLong(estimateId))) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Estimate estimate = estimateRepository.findById(Long.parseLong(estimateId)).get();
            if (!estimate.getClient().getUsername().equals(username)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서의 클라이언트가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            estimateRepository.delete(estimate);
            return ResponseEntity.ok().body(Collections.singletonMap("delete_estimate", "견적서가 삭제되었습니다"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("delete_estimate", "견적서 삭제에 실패했습니다"));
        }

    }

    // 견적을 견적상태dto로 변환
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

    // 견적을 공사로 변환
    private Construction estimateToConstruction(Estimate estimate, ReasonDto reasonDto) {
        Address constructionAddress = Address.builder().
                city(estimate.getSite().getCity())
                .district(estimate.getSite().getDistrict())
                .addressDetail(estimate.getSite().getAddressDetail())
                .build();
        Construction construction = Construction.builder()
                .name(estimate.getName())
                .client(estimate.getClient())
                .contractor(estimate.getContractor())
                .field(estimate.getField())
                .site(constructionAddress)
                .period(estimate.getPeriod())
                .budget(estimate.getBudget())
                .contractContent(estimate.getClientContent())
                .reason(reasonDto.getReason())
                .constructionStatus(ConstructionStatus.WAITING)
                .build();
        return construction;
    }
}
