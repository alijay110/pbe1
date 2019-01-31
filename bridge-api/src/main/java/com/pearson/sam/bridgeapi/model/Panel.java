package com.pearson.sam.bridgeapi.model;

import io.leangen.graphql.annotations.GraphQLIgnore;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "panels")
public class Panel extends BaseModel {

  @Id
  private String id;

  private String panelId;
  private String label;
  private String header;
  private Content content;
  private String link;
  private String type;
  private List<String> viewers;

  public String getPanelId() {
    return panelId;
  }

  public void setPanelId(String panelId) {
    this.panelId = panelId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getViewers() {
    return viewers;
  }

  public void setViewers(List<String> viewers) {
    this.viewers = viewers;
  }

  @GraphQLIgnore
  public String getId() {
    return id;
  }

  @GraphQLIgnore
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Panel [id=" + id + ", panelId=" + panelId + ", label=" + label + ", header=" + header
        + ", content=" + content + ", link=" + link + ", type=" + type + ", viewers=" + viewers
        + "]";
  }

}
