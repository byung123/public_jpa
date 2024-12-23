package security_practice.src.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security_practice.src.aspect.annotation.ValidAop;
import security_practice.src.dto.request.ReqSignupDto;
import security_practice.src.service.SignupService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")

public class AuthenticationController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    @ValidAop
    public ResponseEntity<?> signup(@Valid @RequestBody ReqSignupDto reqDto, BeanPropertyBindingResult result) {
        log.info("{}", reqDto);
        return ResponseEntity.created(null).body(signupService.signup(reqDto));
    }
}
