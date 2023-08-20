package practice.board.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="BOARD_ID")
    private Long id;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<BoardImage> boardImage = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "board")
    private List<Comment> comment = new ArrayList<>();
    public void update(String title, String content) {
        this.title = title;
        this.content = title;
        this.modifiedAt = LocalDateTime.now();
    }
    @Builder
    public Board(String title, String content, Member member,List boardImage,List comment) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.boardImage = boardImage;
        this.comment = comment;
    }
}


