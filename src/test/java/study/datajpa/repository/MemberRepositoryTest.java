package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    void testMember() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        //값이 있을수도, 없을 수도 있으니 Optional로 가져와짐
//        Optional<Member> byId = memberRepository.findById(saveMember.getId());
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member1");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Optional<Member> byId = memberRepository.findById(member1.getId());
        Member findMember1 = byId.get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }


    @Test
    void findByUsernameNadAgeGreaterThen() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    void testNameQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }
    @Test
    void testQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    void findUsernameList() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        member1.setTeam(team);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        member1.setTeam(team);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void returnType() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        member1.setTeam(team);
        memberRepository.save(member1);

        List<Member> aaa = memberRepository.findListByUsername("AAA"); //조회 값이 없을 경우 empty (size = 0)
        Member findMember = memberRepository.findMemberByUsername("AAA"); //조회 값이 없을 경우 null
        Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");
        //조회 값이 없을 경우 empty, 값이 두개일 경우 Ex 발생

        System.out.println("findMember = " + findMember);
    }

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //페이지를 1이 아닌 0부터 시작, 한 페이지에 3개의 데이터를 가져오겠다

        //when

        //page 단점: 성능 자체가 원래 느리다..이유: totalCount 쿼리가 나가기 때문 (join등 쿼리가 길어질 수록 더더더욱 성능이 안좋아짐)
        //성능 향상 방법: memberRepository에 findByAge 메소드에 @Query를 사용 + countQuery를 분리한다.
        //결론: 쿼리가 복잡해지는 경우 countQuery를 사용하여 쿼리를 분리하자! (간단한 쿼리 경우 그냥 사용해도 됨)
        Page<Member> page = memberRepository.findByAge(age, pageRequest); //반환타입을 Page로 받으면 totalCount를 따로 구할 필요가 없음
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest); //totalCount를 구하지 않는다.

        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //then
        List<Member> content = page.getContent(); //page에 있는 컨텐츠 가져오기
        long totalElements = page.getTotalElements(); //이렇게 totalCount를 구해준다 알아서

        for (Member member : content) {
            System.out.println("member = " + member);

        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0 ); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //총 페이지 수
        assertThat(page.isFirst()).isTrue(); //첫번쨰 페이지인지
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는지
    }

}