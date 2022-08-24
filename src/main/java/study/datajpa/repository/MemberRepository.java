package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * JPARepository
 * 메소드명으로 쿼리 생성
 */
public interface MemberRepository extends JpaRepository<Member, Long> { //pk: Long

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") // 장점: 오타가 생겨도 로딩 시점에 에러 발생하여 오류 수정이 쉽다.
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
