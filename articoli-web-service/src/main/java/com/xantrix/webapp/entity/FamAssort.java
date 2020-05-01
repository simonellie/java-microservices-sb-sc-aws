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
@Table(name = "FAMASSORT")
@Getter
@Setter
public class FamAssort implements Serializable {
  @Id
  @Column(name="ID")
  private long id;
  @Column(name="DESCRIZIONE")
  private String descrizione;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "famAssort")
  @JsonBackReference
  private Set<Item> items = new HashSet<>();

  public FamAssort () {
    super();
  }
}
