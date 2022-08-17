package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

/**
 * JPARepository
 */
public interface MemberRepository extends JpaRepository<Member, Long> { //pk: Long
}
