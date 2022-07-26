package com.anodiam.login.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private AnodiamRole name;

  public Role() {

  }

  public Role(AnodiamRole name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public AnodiamRole getName() {
    return name;
  }

  public void setName(AnodiamRole name) {
    this.name = name;
  }
}