package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *@created 29/10/2022
 *@author DELL
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChatInfo {
    @FieldName(name = "unread_number")
    private int unreadNumber;
    @FieldName(name = "last_read_time")
    private String lastReadTime;
    @FieldName(name = "delete_time")
    private Long deleteTime;
}
