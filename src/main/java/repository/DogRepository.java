package repository;

import entity.Dog;

import java.util.List;

/*
 *@created 27/10/2022
 *@author DELL
*/

public interface DogRepository{
    List<Dog> findByName(String name);
    long count(String name);
}
