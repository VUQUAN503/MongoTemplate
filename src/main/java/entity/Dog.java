package entity;

import lombok.*;

/*
 *@created 13/08/2022
 *@author DELL
*/

@AllArgsConstructor
@Entity(databaseName = "dogs_db", collectionName = "dogs")
@NoArgsConstructor
@Data
public class Dog implements AbstractEntity, Comparable<Dog>{
    @FieldName(name = "name")
    private String name;
    @FieldName(name = "color")
    private String color;

    @Override
    public int compareTo(Dog o) {
        return 0;
    }
}
