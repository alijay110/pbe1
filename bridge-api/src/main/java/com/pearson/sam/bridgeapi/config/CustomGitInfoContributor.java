package com.pearson.sam.bridgeapi.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.GitInfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Component;

@Component
public class CustomGitInfoContributor extends GitInfoContributor {

  @Autowired
  public CustomGitInfoContributor(GitProperties properties) {
    super(properties);
  }

  @Override
  public void contribute(Info.Builder builder) {
    Map<String, Object> map = generateContent();
    map.put("tags", getProperties().get("tags"));
    map.put("dirty", getProperties().get("dirty"));
    map.put("branch", getProperties().get("branch"));
    map.put("remote.origin.url", getProperties().get("remote.origin.url"));
    map.put("commit.id", getProperties().get("commit.id"));
    map.put("commit.id.abbrev", getProperties().get("commit.id.abbrev"));
    map.put("commit.id.describe", getProperties().get("commit.id.describe"));
    map.put("commit.id.describe-short", getProperties().get("commit.id.describe-short"));
    map.put("commit.user.name", getProperties().get("commit.user.name"));
    map.put("commit.user.email", getProperties().get("commit.user.email"));
    map.put("commit.message.full", getProperties().get("commit.message.full"));
    map.put("commit.message.short", getProperties().get("commit.message.short"));
    map.put("commit.time", getProperties().get("commit.time"));
    map.put("closest.tag.name", getProperties().get("closest.tag.name"));
    map.put("closest.tag.commit.count", getProperties().get("closest.tag.commit.count"));
    map.put("build.user.name", getProperties().get("build.user.name"));
    map.put("build.user.email", getProperties().get("build.user.email"));
    map.put("build.time", getProperties().get("build.time"));
    map.put("build.host", getProperties().get("build.host"));
    map.put("build.version", getProperties().get("build.version"));

    builder.withDetail("git", map);
  }
}
