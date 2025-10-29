package com.workintech.sqldmljoins.repository;

import com.workintech.sqldmljoins.entity.Ogrenci;
import com.workintech.sqldmljoins.entity.StudentClassCount;
import com.workintech.sqldmljoins.entity.StudentNameCount;
import com.workintech.sqldmljoins.entity.StudentNameSurnameCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OgrenciRepository extends JpaRepository<Ogrenci, Long> {

    // Kitap alan öğrenciler
    @Query(value = """
        SELECT o.ogrno AS ogrno,
               o.ad AS ad,
               o.soyad AS soyad,
               o.cinsiyet AS cinsiyet,
               o.sinif AS sinif,
               o.puan AS puan,
               o.dtarih AS dtarih
        FROM ogrenci o
        WHERE o.ogrno IN (SELECT DISTINCT i.ogrno FROM islem i)
    """, nativeQuery = true)
    List<Ogrenci> findStudentsWithBook();

    // Kitap almayan öğrenciler
    @Query(value = """
        SELECT o.ogrno AS ogrno,
               o.ad AS ad,
               o.soyad AS soyad,
               o.cinsiyet AS cinsiyet,
               o.sinif AS sinif,
               o.puan AS puan,
               o.dtarih AS dtarih
        FROM ogrenci o
        WHERE o.ogrno NOT IN (SELECT DISTINCT i.ogrno FROM islem i)
    """, nativeQuery = true)
    List<Ogrenci> findStudentsWithNoBook();

    // Her sınıftaki kitap alan öğrenci sayısı
    @Query(value = """
        SELECT o.sinif AS sinif,
               COUNT(i.kitapno) AS count
        FROM ogrenci o
        INNER JOIN islem i ON o.ogrno = i.ogrno
        GROUP BY o.sinif
    """, nativeQuery = true)
    List<StudentClassCount> findClassesWithBookCount();

    // Toplam öğrenci sayısı
    @Query(value = "SELECT COUNT(*) FROM ogrenci", nativeQuery = true)
    int findStudentCount();

    // Kaç farklı isimde öğrenci var
    @Query(value = "SELECT COUNT(DISTINCT ad) FROM ogrenci", nativeQuery = true)
    int findUniqueStudentNameCount();

    // Her isimde kaç öğrenci var (interface projection)
    @Query(value = """
        SELECT o.ad AS ad,
               COUNT(*) AS count
        FROM ogrenci o
        GROUP BY o.ad
    """, nativeQuery = true)
    List<StudentNameCount> findStudentNameCount();

    // Her sınıftaki öğrenci sayısı (interface projection)
    @Query(value = """
        SELECT o.sinif AS sinif,
               COUNT(*) AS count
        FROM ogrenci o
        GROUP BY o.sinif
    """, nativeQuery = true)
    List<StudentClassCount> findStudentClassCount();

    // Her öğrencinin ad-soyadına göre aldığı kitap sayısı
    @Query(value = """
        SELECT o.ad AS ad,
               o.soyad AS soyad,
               COUNT(i.kitapno) AS count
        FROM ogrenci o
        LEFT JOIN islem i ON o.ogrno = i.ogrno
        GROUP BY o.ad, o.soyad
    """, nativeQuery = true)
    List<StudentNameSurnameCount> findStudentNameSurnameCount();
}