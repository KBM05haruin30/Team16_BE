package org.cookieandkakao.babting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public Map<String, String> getTestMessage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a test API response.");
        response.put("status", "success");
        return response;
    }
}
