package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * JPARepository
 * 메소드명으로 쿼리 생성
 */
public interface MemberRepository extends JpaRepository<Member, Long> { //pk: Long

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();
}
