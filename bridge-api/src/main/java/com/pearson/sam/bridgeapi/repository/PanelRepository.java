package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Panel;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanelRepository extends MongoRepository<Panel, String> {

  Panel findByPanelId(String panelId);

  List<Panel> findAllByViewersContaining(Set<String> roles);

  Panel findByPanelIdAndViewersIsContaining(String panelId,Set<String> roles);

  
  void deleteByPanelId(String panelId);

}
