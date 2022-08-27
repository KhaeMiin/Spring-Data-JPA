package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO로 반환받기
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //파라미터 바인딩
    @Query("select m from Member m where m.username in :names") //':name' 이름기반 ('?0'이런식의 위치기반은 사용하지말자)
    List<Member> findByNames(@Param("names") Collection<String> names);


    //반환 타입
    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptionalByUsername(String name); //단건 Optional

    //페이징
//    Slice<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") //countQuery로 쿼리를 분리하여 단순히 (join없이) member만 count할 수 있도록
    Page<Member> findByAge(int age, Pageable pageable);

    //벌크성 쿼리
    @Modifying(clearAutomatically = true) //JPA의 executeUpdate()와 같음 (없으면 resultList, singleResult 반환됨)
    //clearAutomatically = true: entitymanager의 clear()와 같은 기능 제공
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    //fetch join
    @Query("select m from Member m left join fetch m.team") //한방 쿼리로 다 끌고옴
    List<Member> findMemberFetchJoin();

    //EntityGraph를 사용하여 fetch join 해결하기
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team"))
    List<Member> findEntityGraphByUsername(@Param("username") String username);

//    @EntityGraph("Member.all") //잘 사용하지 않음
//    List<Member> findEntityGraphByUsername(@Param("username") String username);

    //JPA QueryHint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) //읽기만 가능하게 (변경 감지 사용하지 않는다)
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
