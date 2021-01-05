package com.love.outofmemory.service;

import com.love.outofmemory.domain.Classification;

import java.util.List;

/**
 * @author huang
 */
public interface IClassificationService {


    List<Classification> getClassificationByUserID(Integer id);

    Classification getIdByname(Integer userId,String name);

    int addClassification(Integer userId,String classifyName);

    List<Classification> getDiyClassificationByUserID(Integer id);

    int deleteClassByuidAndclassid(Integer uid, Integer[] classDeleteId);

    List<Classification> getMovclass(Integer uid, Integer currentclass);
}
