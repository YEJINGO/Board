package practice.board.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IMAGE_ID")
    private Long id;
    private String image;
    private String imageKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;
    @Builder
    public BoardImage(String image, String imageKey, Board board) {
        this.image = image;
        this.imageKey = imageKey;
        this.board = board;
    }
    public void update(String image) {
        this.image = image;
    }
}
