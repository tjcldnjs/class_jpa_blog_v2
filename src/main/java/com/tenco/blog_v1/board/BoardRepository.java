package com.tenco.blog_v1.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em;


    /**
     * 게시글 조회 메서드
     * @param id 조회할 게시글 ID
     * @return 조회된 Board 엔티티, 존재하지 않으면 null 반환
     */
    public Board findById(int id) {
        return em.find(Board.class, id);
    }


    /**
     * JPQL의 FETCH 조인 사용 - 성능 최적화
     * 한방에 쿼리를 사용해서 즉, 직접 조인해서 데이터를 가져옵니다.
     * @param id
     * @return
     */
    public Board findByIdJoinUser(int id) {
        // JPQL -> Fetch join 을 사용해 보자.
        String jpql = " SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id ";  // : 명명된 파라미터
        return em.createQuery(jpql,Board.class)
                .setParameter("id",id)
                .getSingleResult();
    }


    /**
     * 모든 게시글 조회
     * @return 게시글 리스트
     */
    public List<Board> findAll() {
        TypedQuery<Board> jpql = em.createQuery(" SELECT b FROM Board b ORDER BY b.id DESC ",Board.class);
        return jpql.getResultList();
    }

    // em.persist(board) -> 비영속 상태인 엔티티를 영속상태로 전환
    @Transactional
    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    /**
     * 게시글 삭제하기
     * @param id
     */
    // DELETE JPA API 메서드를 활용(영속성 컨텍트), JPQL, --> QDSL
    @Transactional // 트랜잭션 내에서 실행되도록 보장
    public void deleteById(int id) {
        Query jpql = em.createQuery(" DELETE FROM Board b WHERE b.id = :id ");
        jpql.setParameter("id", id);
        jpql.executeUpdate();

    }


    @Transactional // 트랜잭션 내에서 실행되도록 보장
    public void deleteByIdJPA(int id) {
        Board board = em.find(Board.class, id);
        em.remove(board);
    }

}
