package com.workintech.sqldmljoins.repository;

import com.workintech.sqldmljoins.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OgrenciRepository extends JpaRepository<Ogrenci, Long> {

    // 10A ve 10B sınıflarındaki öğrencilerin kitap sayısı
    String QUESTION_1 = """
        SELECT o.sinif AS className, 
               COALESCE(COUNT(i.islemno),0) AS count
        FROM ogrenci o
        LEFT JOIN islem i ON o.ogrno = i.ogrno
        WHERE o.sinif IN ('10A','10B')
        GROUP BY o.sinif
    """;
    @Query(value = QUESTION_1, nativeQuery = true)
    List<StudentClassCount> findStudentClassCount();

    // Tekil öğrenci sayısı
    String QUESTION_2 = "SELECT COUNT(DISTINCT ogrno) FROM ogrenci";
    @Query(value = QUESTION_2, nativeQuery = true)
    Integer findUniqueStudentCount();
}