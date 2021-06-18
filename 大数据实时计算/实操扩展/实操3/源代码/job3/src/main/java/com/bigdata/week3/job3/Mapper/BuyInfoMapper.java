package com.bigdata.week3.job3.Mapper;

import com.bigdata.week3.job3.Model.BuyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BuyInfoMapper {
    public List<BuyInfo>listBuyInfos();
}
