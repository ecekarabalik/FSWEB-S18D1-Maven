package com.workintech.sqldmljoins.repository;

import com.workintech.sqldmljoins.entity.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KitapRepository extends JpaRepository<Kitap, Long> {

    // Dram ve Hikaye türündeki kitaplar
    @Query(value = """
        SELECT k.kitapno AS kitapno,
               k.ad AS ad,
               k.puan AS puan,
               k.yazarno AS yazarno,
               k.turno AS turno
        FROM kitap k
        INNER JOIN tur t ON k.turno = t.turno
        WHERE t.ad IN ('Dram', 'Hikaye')
    """, nativeQuery = true)
    List<Kitap> findBooks();

    // Tüm kitapların ortalama puanı
    @Query(value = "SELECT AVG(puan) FROM kitap", nativeQuery = true)
    Double findAvgPointOfBooks();
}