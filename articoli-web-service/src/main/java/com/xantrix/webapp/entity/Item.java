package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ARTICOLI")
@Getter
@Setter
public class Item implements Serializable {
  @Id
  @Column(name="CODART")
  private String codArt;
  @Column(name="DESCRIZIONE")
  private String descrizione;
  @Column(name="UM")
  private String um;
  @Column(name="CODSTAT")
  private String codStat;
  @Column(name="PZCART")
  private long pzCart;
  @Column(name="PESONETTO")
  private double pesoNetto;
  @Column(name="IDSTATOART")
  private String idStatoArt;
  @Column(name="DATACREAZIONE")
  private java.sql.Date dataCreaz;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
  @JsonManagedReference
  private Set<Barcode> barcode = new HashSet<>();

  @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
  private Ingredient ingredient;

  @ManyToOne
  @JoinColumn(name = "IDIVA", referencedColumnName = "idIva")
  private Iva iva;

  @ManyToOne
  @JoinColumn(name = "IDFAMASS", referencedColumnName = "id")
  private FamAssort famAssort;

  public Item () {
    super();
  }

  public Item(String codArt, String descrizione, int pzCart, double pesoNetto, String idStatoArt) {
    this.codArt = codArt;
    this.descrizione = descrizione;
    this.pzCart = pzCart;
    this.pesoNetto = pesoNetto;
    this.idStatoArt = idStatoArt;
  }
}
