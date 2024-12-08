package ca.gbc.approvalservice.client;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
class UserCllientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public boolean isValid(String userId) {
                System.out.println("helloooo");
                // Log the cause of the failure
                if (cause != null && cause instanceof feign.RetryableException) {
                    System.out.println("Network failure or timeout: " + cause.getMessage());
                } else if (cause != null && cause instanceof feign.FeignException) {
                    feign.FeignException feignException = (feign.FeignException) cause;
                    if (feignException.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                        System.out.println("Server error (500) occurred while calling isValid: " + cause.getMessage());
                    }
                }

                // Provide a fallback response
                return false; // Default response during a failure
            }

            @Override
            public boolean isUserStaff(String userId) {
                // Log the cause of the failure
                if (cause != null && cause instanceof feign.RetryableException) {
                    System.out.println("Network failure or timeout: " + cause.getMessage());
                } else if (cause != null && cause instanceof feign.FeignException) {
                    feign.FeignException feignException = (feign.FeignException) cause;
                    if (feignException.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                        System.out.println("Server error (500) occurred while calling isUserStaff: " + cause.getMessage());
                    }
                }

                // Provide a fallback response
                return false; // Default response during a failure
            }
        };
    }
}
