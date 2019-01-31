package com.pearson.sam.bridgeapi.model;

import io.leangen.graphql.execution.SortField.Direction;

/**
 * Sort model.
 * @author VKuma09
 *
 */
public class Sort {

  private String field;

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public Direction getOrder() {
    return order;
  }

  public void setOrder(Direction order) {
    this.order = order;
  }

  private Direction order;

}
