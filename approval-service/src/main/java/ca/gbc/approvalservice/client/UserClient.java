package ca.gbc.approvalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-client", url = "${user.service.url}/api/users" , fallbackFactory = UserCllientFallback.class)
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value="/isValid")
    boolean isValid(@RequestParam("userId") String userId);

    @RequestMapping(method = RequestMethod.GET, value="/isUserStaff")
    boolean isUserStaff(@RequestParam("userId") String userId);
}

