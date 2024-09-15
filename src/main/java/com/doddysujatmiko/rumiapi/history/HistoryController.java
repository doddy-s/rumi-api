package com.doddysujatmiko.rumiapi.history;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
import com.doddysujatmiko.rumiapi.history.dtos.PostHistoryReqDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/history")
@Tag(name = "History")
public class HistoryController {
    private final Responser responser;
    private final HistoryService historyService;

    @Autowired
    public HistoryController(Responser responser, HistoryService historyService) {
        this.responser = responser;
        this.historyService = historyService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getHistory(
            Principal principal,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "25") Integer size
    ) {
        return responser.response(HttpStatus.OK, "Success",
                historyService.readAuthenticatedUserHistory(principal, page, size));
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getSomeoneHistory(
            @PathVariable String username,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "25") Integer size
    ) {
        return responser.response(HttpStatus.OK, "Success",
                historyService.readSomeoneHistory(username, page, size));
    }

    @PostMapping("/")
    public ResponseEntity<?> postHistory(Principal principal, @RequestBody PostHistoryReqDto req) {
        return responser.response(HttpStatus.OK, "Success", historyService.createHistory(principal, req));
    }
}
