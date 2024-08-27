package com.doddysujatmiko.rumiapi.auth;

import com.doddysujatmiko.rumiapi.auth.dtos.LoginReqDto;
import com.doddysujatmiko.rumiapi.auth.dtos.RegisterReqDto;
import com.doddysujatmiko.rumiapi.auth.schemas.ErrorSchema;
import com.doddysujatmiko.rumiapi.auth.schemas.Login200Schema;
import com.doddysujatmiko.rumiapi.auth.schemas.Register201Schema;
import com.doddysujatmiko.rumiapi.auth.utils.TokenGenerator;
import com.doddysujatmiko.rumiapi.common.utils.Responser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    Responser responser;

    @Operation(summary = "Register New User",
            description = " ")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Register201Schema.class))},
                    description = "User created"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorSchema.class))},
                    description = "See message for details"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReqDto req) {
        return responser.response(HttpStatus.CREATED, "Register success", authService.register(req));
    }

    @Operation(summary = "Login Existing User",
            description = " ")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Login200Schema.class))},
                    description = "Login success"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorSchema.class))},
                    description = "See message for details"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto req) {
        return responser.response(HttpStatus.OK, "Login success",
                tokenGenerator.createToken(authService.login(req)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/whoami")
    public ResponseEntity<?> getAuthenticatedUser(Principal principal) {
        return responser.response(HttpStatus.OK, "This is you", authService.readAuthenticatedUser(principal));
    }
}
