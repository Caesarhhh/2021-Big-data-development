package com.bigdata.week3.job4.Service;

import com.bigdata.week3.job4.Mapper.BuyInfoMapper;
import com.bigdata.week3.job4.Model.BuyInfo;
import com.bigdata.week3.job4.Mapper.BuyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BuyInfoService {

    @Autowired
    private BuyInfoMapper buyInfoMapper;

    public List<BuyInfo> getBuyInfoList(){
        return buyInfoMapper.listBuyInfos();
    }

    public void addBuyInfo(BuyInfo buyInfo,String table){
        HashMap hashMap=new HashMap();
        hashMap.put("table",table);
        hashMap.put("username",buyInfo.getUsername());
        hashMap.put("buy_time",buyInfo.getBuy_time());
        hashMap.put("buy_address",buyInfo.getBuy_address());
        hashMap.put("origin",buyInfo.getOrigin());
        hashMap.put("destination",buyInfo.getDestination());
        buyInfoMapper.addBuyInfos(hashMap);
    }
}
