package dao;

import java.util.List;

public interface CRUDOperacije<T> {
    void kreiraj(T obj);
    List<T> dohvatiSve();
    void azuriraj(T obj);
    void brisi(int id);
}
