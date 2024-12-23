package com.study.SpringSecurity.repository;

import com.study.SpringSecurity.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // IOC에 등록하려면 원래 클래스여야 하는데 JPA라서 가능하다?
public interface UserRepository extends JpaRepository<User, Long> {

    // Optional : User 자료형을 return을 하는데 만약 null값이면
    Optional<User> findByUsername(String username);
}
