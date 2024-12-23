package security_practice.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security_practice.src.domain.entity.User;
import security_practice.src.dto.request.ReqSignupDto;
import security_practice.src.repository.UserRepository;

@Service
public class SignupService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User signup(ReqSignupDto reqDto) {
        User user = reqDto.toEntity(passwordEncoder);

        return userRepository.save(user);
    }

    public boolean isDuplicatedUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
