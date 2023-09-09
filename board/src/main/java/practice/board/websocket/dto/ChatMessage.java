package practice.board.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message,long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;

    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private long userCount;
}

