package dat3.dao;

import java.util.List;

public interface IDao<T, D> {

    T read(Class<T> tClass, D d);
    List<T> readAll(Class<T> tClass);
    T create(T t);
    T update(T t);
    void delete(Class<T> tClass, D d);
    boolean validatePrimaryKey(Class<T> tClass, D d);

}
