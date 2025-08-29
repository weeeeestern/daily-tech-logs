package com.weeeeestern.dtlogs.slack;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
public class SlackController {

    @PostMapping("/category")
    public ResponseEntity<String> category() {
        // TODO: handle /카테고리 command
        return ResponseEntity.ok("TODO");
    }

    @PostMapping("/summary")
    public ResponseEntity<String> summary() {
        // TODO: handle /정리 command
        return ResponseEntity.ok("TODO");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        // TODO: handle /초기화 command
        return ResponseEntity.ok("TODO");
    }
}
