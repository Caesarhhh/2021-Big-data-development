package com.bigdata.week3.job4.Mapper;

import com.bigdata.week3.job4.Model.BuyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BuyInfoMapper {
    public List<BuyInfo>listBuyInfos();
    public void addBuyInfos(HashMap map);
}
