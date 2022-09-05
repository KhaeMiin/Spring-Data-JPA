package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void save() {
        /**
         * SimpleJpaRepository에서 save() Method에서 조건문에 디버깅을 걸었을 때
         * 식별자가 객체일 경우(GeneratedValue 사용하지 않고 PK값 직접 입력시) null이 아닌 이상 em.persist가 작동하지 않고 값("A")이 있다고 판단하여
         * em.merge()로 가는 것을 볼 수 있다.
         * 하지만 "A"를 디비에 검색시 값이 없기 때문에
         * 다시 em.persist를 하는 것을 볼 수 있다. (쿼리문이 select > insert) 아주 비효율적
         *
         * 그래서 Persistable<String>을 엔티티에서 implement하여 boolean isNew(){}에 새로운 엔티티인지 아닌지 구별할 수 있는 로직을 직접 짜줘야 한다.
         * 다시 디버깅을 해보면 em.persist()로 넘어가는 것을 볼 수 있다.
         */
        Item item = new Item("A");
        itemRepository.save(item);
    }

}