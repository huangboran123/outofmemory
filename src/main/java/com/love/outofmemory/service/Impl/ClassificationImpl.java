package com.love.outofmemory.service.Impl;

import com.love.outofmemory.domain.Classification;
import com.love.outofmemory.mapper.ClassificationMapper;
import com.love.outofmemory.service.IClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huang
 */
@Service
public class ClassificationImpl implements IClassificationService {
    @Autowired
    private ClassificationMapper classificationMapper;

    @Override
    public List<Classification> getClassificationByUserID(Integer id) {
        List<Classification> list=classificationMapper.getClassificationByUserID(id);
        if(list.size()==0){
            classificationMapper.addClassification(id,"默认分类");
        }
        return classificationMapper.getClassificationByUserID(id);


    }

    @Override
    public Classification getIdByname(Integer userId,String name) {

        return classificationMapper.getIdByname(userId,name);
    }

    @Override
    public int addClassification(Integer userId,String classifyName) {

        if(classifyName==null||userId==null){
            return 0;
        }
        return classificationMapper.addClassification(userId,classifyName);
    }

    @Override
    public List<Classification> getDiyClassificationByUserID(Integer id) {
        return classificationMapper.getDiyClassificationByUserID(id);
    }

    @Override
    public int deleteClassByuidAndclassid(Integer uid, Integer[] classDeleteId) {
        int i=0;
        for (Integer c:classDeleteId) {
            if(classificationMapper.deleteClassByuidAndclassid(uid,c)==1){
                i++;
            }
        }
        return i;
    }

    @Override
    public List<Classification> getMovclass(Integer uid, Integer currentclass) {
        return classificationMapper.getMovclass(uid,currentclass);
    }
}
