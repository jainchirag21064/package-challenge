package com.mobiquity.packer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model class to hold each things which is defined with weight,cost and index number
 */
@Data
@AllArgsConstructor
public class Item implements Comparable<Item> {

  private Integer index;
  private Double weight;
  private Integer cost;

  @Override
  public int compareTo(Item o) {
    if (this.cost.equals(o.cost)) {
      return this.weight > o.weight ? 1 : -1;
    } else {
      return this.cost > o.cost ? -1 : 1;
    }
  }
}
