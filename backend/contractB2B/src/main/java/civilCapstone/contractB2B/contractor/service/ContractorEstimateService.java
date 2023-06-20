package civilCapstone.contractB2B.contractor.service;

import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.estimate.entity.Estimate;
import civilCapstone.contractB2B.estimate.entity.EstimateStatus;
import civilCapstone.contractB2B.estimate.model.EstimateDto;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.estimate.repository.EstimateRepository;
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

@Slf4j
@Service
// 하청업체 견적서 관련 Service
public class ContractorEstimateService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EstimateRepository estimateRepository;

    // 하청업체 견적 조회 요청 처리
    public ResponseEntity getEstimate(String username) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_estimate", "사용자가 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            User user = userRepository.findByUsername(username).get();
            if (! estimateRepository.existsByContractor(user)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_estimate", "견적서가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            List<EstimateDto> estimates = new ArrayList<>();
            for (Estimate estimate : estimateRepository.findAllByContractor(userRepository.findByUsername(username).get())) {
                EstimateDto estimateResponseDto = getEstimateGetDto(estimate);
                estimates.add(estimateResponseDto);
            }
            return ResponseEntity.ok().body(Collections.singletonMap("get_estimate", estimates));
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_estimate", "견적서를 불러오는데 실패했습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 하청업체 원견적 요청 처리
    public ResponseEntity getMotherEstimate(String username) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "사용자가 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            User user = userRepository.findByUsername(username).get();
            if (! estimateRepository.existsByContractor(user)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "견적서가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            List<EstimateDto> estimates = new ArrayList<>();
            for (Estimate estimate : estimateRepository.findAllByContractor(userRepository.findByUsername(username).get())) {
                if (estimate.getMotherId() == null) {
                    EstimateDto estimateResponseDto = getEstimateGetDto(estimate);
                    estimates.add(estimateResponseDto);
                }
            }
            return ResponseEntity.ok().body(Collections.singletonMap("get_estimate", estimates));
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_mother_estimate", "부모 견적서를 불러오는데 실패했습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 하청업체 재견적 조회 요청 처리
    public ResponseEntity getChildEstimate(String username, String motherId) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_child_estimate", "사용자가 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            List<EstimateDto> estimates = new ArrayList<>();
            if (!estimateRepository.existsByMotherId(motherId)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_child_estimate", "하위 견적이 존재하지 않습니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            for (Estimate estimate : estimateRepository.findAllByContractor(userRepository.findByUsername(username).get())) {
                if (estimate.getMotherId().equals(motherId)) {
                    EstimateDto estimateResponseDto = getEstimateGetDto(estimate);
                    estimates.add(estimateResponseDto);
                }
            }
            return ResponseEntity.ok().body(Collections.singletonMap("get_child_estimate", estimates));
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_child_estimate", "하위 견적서를 불러오는데 실패했습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 하청업체 견적 수락 요청 처리
    @Transactional
    public ResponseEntity acceptEstimate(String username, String estimateId) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("accept_estimate", "사용자가 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (!estimateRepository.existsById(Long.parseLong(estimateId))) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("accept_estimate", "견적서가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Estimate estimate = estimateRepository.findById(Long.parseLong(estimateId)).get();
            if (! estimate.getContractor().getUsername().equals(username)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("accept_estimate", "견적서의 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            estimate.setEstimateStatus(EstimateStatus.PENDING);
            estimate.setContractorContent(estimate.getClientContent());
            estimateRepository.save(estimate);
            EstimateDto estimateStatusResponseDto = getEstimateStatusDto(estimate);
            return ResponseEntity.ok().body(estimateStatusResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("accept_estimate", "견적서를 수락하는데 실패했습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 하청업체 견적 거절 요청 처리
    public ResponseEntity rejectEstimate(String username, String estimateId) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("reject_estimate", "사용자가 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (!estimateRepository.existsById(Long.parseLong(estimateId))) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("reject_estimate", "견적서가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Estimate estimate = estimateRepository.findById(Long.parseLong(estimateId)).get();
            if (! estimate.getContractor().getUsername().equals(username)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("reject_estimate", "견적서의 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            estimate.setEstimateStatus(EstimateStatus.REJECTED);
            estimateRepository.save(estimate);
            EstimateDto estimateStatusResponseDto = getEstimateStatusDto(estimate);
            return ResponseEntity.ok().body(estimateStatusResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("reject_estimate", "견적서를 거절하는데 실패했습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 하청업체 재견적 요청 처리
    @Transactional
    public ResponseEntity createChildEstimate(String username, String motherId, EstimateDto.ContractorChildEstimateRequestDto estimateDto) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CONTRACTOR) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_child_estimate", "사용자가 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (!estimateRepository.existsById(Long.parseLong(motherId))) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_child_estimate", "부모 견적서가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Estimate motherEstimate = estimateRepository.findById(Long.parseLong(motherId)).get();
            if (!motherEstimate.getContractor().getUsername().equals(username)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_child_estimate", "부모 견적서의 하청업체가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            motherEstimate.setEstimateStatus(EstimateStatus.REJECTED);
            Address childAddress = Address.builder()
                    .city(motherEstimate.getSite().getCity())
                    .district(motherEstimate.getSite().getDistrict())
                    .addressDetail(motherEstimate.getSite().getAddressDetail())
                    .build();
            Estimate estimate = Estimate.builder()
                    .name(motherEstimate.getName())
                    .client(motherEstimate.getClient())
                    .contractor(motherEstimate.getContractor())
                    .site(childAddress)
                    .field(motherEstimate.getField())
                    .period(estimateDto.getPeriod())
                    .budget(estimateDto.getBudget())
                    .clientContent(motherEstimate.getClientContent())
                    .contractorContent(estimateDto.getContractorContent())
                    .motherId(motherId)
                    .estimateStatus(EstimateStatus.PENDING)
                    .build();
            estimateRepository.save(estimate);
            EstimateDto estimateResponseDto = getEstimateCreateDto(estimate);
            return ResponseEntity.ok().body(estimateResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_child_estimate", "하위 견적서를 생성하는데 실패했습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 견적을 견적 상태dto로 변환
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

    // 견적을 견적 생성dto로 변환
    private EstimateDto getEstimateCreateDto(Estimate estimate) {
        EstimateDto estimateResponseDto = EstimateDto.builder()
                .id(estimate.getId())
                .name(estimate.getName())
                .clientName(estimate.getClient().getName())
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

    // 견적을 견적 조회dto로 변환
    private EstimateDto getEstimateGetDto(Estimate estimate) {
        EstimateDto estimateResponseDto = EstimateDto.builder()
                .id(estimate.getId())
                .name(estimate.getName())
                .clientName(estimate.getClient().getName())
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
}
