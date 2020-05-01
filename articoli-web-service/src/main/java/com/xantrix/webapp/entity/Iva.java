package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "IVA")
@Getter
@Setter
public class Iva implements Serializable {
  @Id
  @Column(name="IDIVA")
  private long idIva;
  @Column(name="DESCRIZIONE")
  private String descrizione;
  @Column(name="ALIQUOTA")
  private long aliquota;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "iva")
  @JsonBackReference
  private Set<Item> items = new HashSet<>();

  public Iva () {
    super();
  }

}
