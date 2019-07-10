package com.lkc.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @RequestMapping(value = "/handler/threshold",method = RequestMethod.POST)
    void thresholdHandler(@RequestParam String deviceId, @RequestBody Map<String, String> map, @RequestParam String time);

    @RequestMapping(value = "/send/realtime", method = RequestMethod.POST)
    void notice();
}
