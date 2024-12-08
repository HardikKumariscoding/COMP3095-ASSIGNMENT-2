package ca.gbc.approvalservice.service;


import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final Logger LOG =  LoggerFactory.getLogger(ApprovalService.class);


    @Autowired
    public ApprovalService(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    // Method to create a new approval
    public ApprovalResponse createApproval(ApprovalRequest request) {
        Approval approvalEntity = Approval.builder()
                .eventId(request.eventId())
                .approvedId(request.approvedId())
                .approved(request.approved())
                .approvalDate(LocalDateTime.now())
                .build();

        Approval savedApproval = approvalRepository.save(approvalEntity);
        LOG.info("Approval created");
        return new ApprovalResponse(
                savedApproval.getId(),
                savedApproval.getEventId(),
                savedApproval.getApprovedId(),
                savedApproval.isApproved(),
                savedApproval.getApprovalDate()
        );
    }

    // Method to get an approval by ID
    public Optional<ApprovalResponse> getApprovalById(String id) {
        LOG.info("Get Approval by ID");
        return approvalRepository.findById(id)
                .map(approval -> new ApprovalResponse(
                        approval.getId(),
                        approval.getEventId(),
                        approval.getApprovedId(),
                        approval.isApproved(),
                        approval.getApprovalDate()
                ));
    }

    // Method to get all approvals
    public List<ApprovalResponse> getAllApprovals() {
        LOG.info("Get all Approvals");
        return approvalRepository.findAll().stream()
                .map(approval -> new ApprovalResponse(
                        approval.getId(),
                        approval.getEventId(),
                        approval.getApprovedId(),
                        approval.isApproved(),
                        approval.getApprovalDate()
                ))
                .collect(Collectors.toList());
    }

    // Method to update an approval
    public Optional<ApprovalResponse> updateApproval(String id, ApprovalRequest request) {
        LOG.info("Update Approval");
        return approvalRepository.findById(id).map(existingApproval -> {
            existingApproval.setEventId(request.eventId());
            existingApproval.setApprovedId(request.approvedId());
            existingApproval.setApproved(request.approved());
            existingApproval.setApprovalDate(LocalDateTime.now());

            Approval updatedApproval = approvalRepository.save(existingApproval);

            return new ApprovalResponse(
                    updatedApproval.getId(),
                    updatedApproval.getEventId(),
                    updatedApproval.getApprovedId(),
                    updatedApproval.isApproved(),
                    updatedApproval.getApprovalDate()
            );
        });
    }

    // Method to delete an approval by ID
    public void deleteApproval(String id) {

        LOG.info("Approval deleted");approvalRepository.deleteById(id);
    }
}
