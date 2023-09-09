package practice.board.member;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.board.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByName(String name);
    Optional<Member> findByName(String name);
    Member findByEmail(String email);

}
