package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INGREDIENTI")
@Getter
@Setter
public class Ingredient implements Serializable {
  @Id
  @Column(name="CODART")
  private String codArt;
  @Column(name="INFO")
  private String info;

  @OneToOne
  @PrimaryKeyJoinColumn
  @JsonIgnore
  private Item item;

  public Ingredient () {
    super();
  }
}
