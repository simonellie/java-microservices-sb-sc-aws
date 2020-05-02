package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "BARCODE")
@Getter
@Setter
public class Barcode implements Serializable {
  @Id
  @Column(name="BARCODE")
  @NotNull(message = "{NotNull.Barcode.barcode.Validation}")
  @Size(min = 8, max = 13, message = "{Size.Barcode.barcode.Validation}")
  private String barcode;
  @Column(name="IDTIPOART")
  @NotNull(message = "{NotNull.Barcode.idTipoArt.Validation}")
  private String idTipoArt;

  @ManyToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "CODART", referencedColumnName = "codArt")
  @JsonBackReference
  private Item item;

  public Barcode () {
    super();
  }

  public Barcode(Item item, String barcode, String idTipoArt) {
    this.item = item;
    this.barcode = barcode;
    this.idTipoArt = idTipoArt;
  }
}
