package entity;

/*
 *@created 29/10/2022
 *@author DELL
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(collectionName = "conversation", databaseName = "chat_log")
public class Conversation {
    @FieldName(name = "conversation_id")
    private String conversationId;
    @FieldName(name = "last_chat_time")
    private String lastChatTime;
    @FieldName(name = "messages")
    private List<Message> messages;
    @FieldName(name = "users")
    private List<String> users;
    private UserChatInfo sender;
    private UserChatInfo receiver;
}
