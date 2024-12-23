package com.study.SpringSecurity.domain.entity;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // 이 어노테이션을 다는 순간 부터 이 클래스는 table 형태가 됨
@Data
public class User {

    @Id // primary 키 값이라고 설정해주는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id를 auto increment 해주겠다
    private Long id;
    @Column(unique = true, nullable = false) // username은 unique여야 한다. 컬럼들이기 때문에 컬럼 어노테이션 사용
    private String username;
    @Column(nullable = false) // 안 속성 nullable을 설정 안하면 기본 값이 true임
    private String password;
    @Column(nullable = false)
    private String name;

    // USER에게 여러 권한들을 넣기 위해 권한 배열을 만듦 ex) admin은 모든 권한을 가져야 하기 때문에 이런 용도로 사용
    // 아무것도 안하면 밑의 테이블을 기준으로 뭘하는 거 같음, 여러 테이블과 관계를 맺을 수 있는 어노테이션
    // OneToMany도 있음 : 이건 테이블과 1대 다 관계로만 맺을 수 있다는 뜻
    // One이면 테이블
    // User를 기준으로 ManyToMany를 하기 위해 User 있는 곳에 ManyToMany를 씀(user가 있음으로 role이 있기 때문에
    // 여기선 여러(Many) 유저들이 여러(Many) 권한 관계를 맺을 수 있기 때문에 ManyToMany를 사용한다

    // fetch : 엔티티를 조인했을 때 연관된 데이터를 언제 가져올지 결정(EAGER - 당장, LAZY - 나중에 사용할 때)
    // EAGER : 애초에 처음부터 조인했을 때
    // LAZY(기본값) : 실제로 데이터를 쓸 때 조인해서 가져옴
    // cascade : 부모를 insert, delete, update를 할 때 하위까지 같이 가게 하는것
    // CascadeType.All -> 자식 테이블 하나하나 참조되는 모든 하위 테이블을 전부 하나씩 삭제해간다는 뜻.
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            // 테이블 이름
            name = "user_roles",
            // 조인 된 테이블의 키값을 user_id로 잡겠다
            joinColumns = @JoinColumn(name = "user_id"), // JPA는 알아서 user_id 라는 것을 만들어줘서 직접 만든적이 없어도 이렇게 쓸 수 있다
            // 외래키로 role_id를 잡겠다
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles; // 중복 제거를 하기 위해 Set으로 받아야한다, 안그러면 ManytoMany에 의해서 모든 경우의 수로 다 묶여짐

//    @OneToMany(mappedBy = "user") // user 테이블과 조인한다는 뜻?
//    private Set<UserRole> userRoles = new HashSet<>(); // 초기화과정, 덮어 쓸 수도 있고, null 오류가 안뜨기 위해서
    // 안되서 주석 처리하고 위에꺼 다시함

    public PrincipalUser toPrincipalUser() {
        return PrincipalUser.builder()
                .userId(id)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }
}
