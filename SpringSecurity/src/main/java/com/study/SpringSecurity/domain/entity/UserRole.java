//package com.study.SpringSecurity.domain.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//// User와 Role의 Many to Many를 수동으로 만들자
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Entity
//public class UserRole {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    // 변수 이름을 id로 설정하면 나중에 조인할 때 jpa가 알아서 테이블명 + id(스네이크 표기) 값으로 컬럼명을 만들어준다
//    // 그래서 따로 설정하지 않아도 밑의 JoinColumn에 name을 user_id, role_id로 설정해 놔도 무방하다
//
//    @ManyToOne // UserRole 입장에선 ManytoOne이기 때문에 이 어노테이션을 달아야 한다
//    @JoinColumn(name = "user_id")
//    private User user; // User 테이블 -> User 테이블 입장에서 UserRole 테이블은 OneToMany 관계가 된다.
//                        // UserRole은 값 자체가 User_id와 Role_id가 합쳐진 쌍이기 떄문
//
//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role; // Role 테이블 -> User 테이블 입장에서 UserRole 테이블은 OneToMany 관계가 된다.
//
//}

// 자꾸 오류나서 지움