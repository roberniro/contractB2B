package civilCapstone.contractB2B.contractor.controller;

import civilCapstone.contractB2B.contractor.model.ExperienceDto;
import civilCapstone.contractB2B.contractor.service.ContractorExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contractor")
public class ContractorExperienceController {
    @Autowired
    private ContractorExperienceService contractorExperienceService;

    @GetMapping("/experience")
    public ResponseEntity getExperience(@AuthenticationPrincipal String username) {
        return contractorExperienceService.getExperience(username);
    }
    @PostMapping("/experience")
    public ResponseEntity createExperience(@AuthenticationPrincipal String username, @RequestBody ExperienceDto experienceDto) {
        return contractorExperienceService.createExperience(username, experienceDto);
    }
}
