package com.bigdata.week3.job3.Service;

import com.bigdata.week3.job3.Mapper.BuyInfoMapper;
import com.bigdata.week3.job3.Model.BuyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyInfoService {

    @Autowired
    private BuyInfoMapper buyInfoMapper;

    public List<BuyInfo> getBuyInfoList(){
        return buyInfoMapper.listBuyInfos();
    }
}
