package ca.gbc.approvalservice.controller;

import ca.gbc.approvalservice.client.EventClient;
import ca.gbc.approvalservice.client.UserClient;
import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/approvals")
@AllArgsConstructor

public class ApprovalController {

    private final ApprovalService approvalService;
    private final UserClient userClient;
    private final EventClient eventClient;

    @Operation(
            summary = "Endpoint to create a new approval",
            description = "Endpoint to create a new approval "
    )

    @PostMapping
    public ResponseEntity<ApprovalResponse> createApproval(@RequestBody ApprovalRequest request) {
        System.out.println("-------------------");
        System.out.println(userClient.isValid(request.approvedId()));
        try {
            System.out.println(userClient);
            if (!userClient.isValid(request.approvedId())) {
                throw new RuntimeException("User is invalid");
            }

            if (!userClient.isUserStaff(request.approvedId())) {
                throw new RuntimeException("Invalid user role.");
            }
        }
        catch (Exception e) {
            // Log when fallback is triggered
           // logger.warn("Fallback triggered due to exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }


        if(!eventClient.isEventValid(request.eventId())) {
            throw new RuntimeException("Invalid event id.");
        }
        String status = request.approved() ? "approved" : "rejected";
        eventClient.updateEventStatus(request.eventId(), status);
        ApprovalResponse approvalResponse = approvalService.createApproval(request);
        return new ResponseEntity<>(approvalResponse, HttpStatus.CREATED);
    }



    @Operation(
            summary = "Endpoint to get approval by ID",
            description = "Endpoint to get approval by ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApprovalResponse> getApprovalById(@PathVariable String id) {
        Optional<ApprovalResponse> approvalResponse = approvalService.getApprovalById(id);
        return approvalResponse
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Endpoint to get all approvals",
            description = "Endpoint to get all approvals"
    )
    @GetMapping
    public ResponseEntity<List<ApprovalResponse>> getAllApprovals() {
        List<ApprovalResponse> approvals = approvalService.getAllApprovals();
        return new ResponseEntity<>(approvals, HttpStatus.OK);
    }

    @Operation(
            summary = "Endpoint to get update an  approval",
            description = "Endpoint to update an approval"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApprovalResponse> updateApproval(
            @PathVariable String id,
            @RequestBody ApprovalRequest request) {
        Optional<ApprovalResponse> updatedApproval = approvalService.updateApproval(id, request);
        return updatedApproval
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Endpoint to get delete an approval",
            description = "Endpoint to delete an approval"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproval(@PathVariable String id) {
        approvalService.deleteApproval(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
