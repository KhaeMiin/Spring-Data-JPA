package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED) //접근제어자 복습: 같은 패키지, 다른 패키지 자손까지
@ToString(of = {"id", "username", "age"})
@NamedQuery( //잘 사용하지 않음 (하지만 장점: 쿼리 오타시 애플리케이션 실행시점에 오류 알려준다.(creatQuery: 오타가 있어도 그냥 실행됨 > 오류 찾기 힘들다.)
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
public class Member {

    @Id
    @GeneratedValue //pk값을 jpa가 알아서
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    /**
     * 연관관계 메서드
     */
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); //Team에 있는 member에도 걸어준다.
    }
}
