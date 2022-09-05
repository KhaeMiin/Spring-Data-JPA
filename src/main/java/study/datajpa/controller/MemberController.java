package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //도메인 클래스 컨버터(pk로 조회하여 엔티티 객체를 반환) > 하지만 이렇게 단순한 경우가 아닌 이상 사용하지 않을 것 같음
    //트렌젝션이 없는 상황에서 엔티티를 조회했기 때문에 데이터를 변경해도 DB에 반영되지 않는다.
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * web 확장 : 페이징과 정렬
     * 한 페이지당 20개의 데이터를 꺼낸다. (localhost:8080/members?page=0) > 페이지는 0페이지부터 시작 (page 안넣어도 default 값이 20이라 20개만 나옵니다)
     * 한 페이지당 출력할 데이터의 갯수를 조절할 수 있다. (localhost:8080/members?page=0&size=3) > 페이지당 데이터 3개만
     * 내림차순~오름차순 조건을 걸 수도 있다 (localhost:8080/members?page=0&sort=id,desc&sort=username,desc)
     * 한 페이지당 나오는 데이터값이 default가 20개이다. 이 부분을 size=? 없이 바꾸려면 application.yml(or properties)에서 바꿀 수 있다.
     *   data: web: pageable: default-page-size: 10 max-page-size: 2000 #최대 페이지값
     *   혹은 @PageableDefault 어노테이션을 사용하여 size와 sort를 설정할 수 있다. (우선권을 가진다)
     *
     *  yml에         one-indexed-parameters: true 를 추가하면 페이지가 1부터 시작하게 할 수 있다.
     *  하지만 파라미터를 -1처리할 뿐이기 때문에 pageable에 나오는 정보와 맞지 않게 된다.
     *  그러므로 깔끔하게 0부터 페이지가 시작하게 사용하자..
     *
     * @param pageable
     * @return
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);//PagingAndSortingRepository interface
        return page.<MemberDto>map(MemberDto::new);
    }

//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
