package com.xantrix.webapp.repository;

import com.xantrix.webapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {
    Item findByCodArt (String codArt);
    List<Item> findByDescrizioneLike(String descrizione);

    //JPQL
    @Query(value = "SELECT i FROM Item i JOIN i.barcode b WHERE b.barcode IN (:ean)")
    Item SelByEan(@Param("ean") String ean);

    /* SQL standard
    * @Query(value = "SELECT * FROM Item i JOIN Barcode b ON i.barcode = b.codart WHERE b.barcode = :ean", nativeQuery = true)
    * Item SelByEan(@Param("ean") String ean);
    * */
}
