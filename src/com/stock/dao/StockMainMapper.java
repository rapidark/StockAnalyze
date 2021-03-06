package com.stock.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.stock.model.CacheItem;
import com.stock.model.StockAnalyseBase;
import com.stock.model.StockAnalyseResult;
import com.stock.model.StockMain;
import com.stock.model.StockMainAnalyse;
import com.stock.model.StockQuery;
import com.stock.model.StockTop100;
import com.stock.util.CodesArrayList;

public interface StockMainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Map<String, Object> map);

    int insertSelective(StockMain record);

    StockMain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockMain record);

    int updateByPrimaryKey(StockMain record);

	List<StockMain> dataList(StockQuery query);

	int getTotal(StockQuery query);
	
	List<StockMain> dataList1(StockQuery query);

	int getTotal1(StockQuery query);

	List<StockMain> showChart(StockQuery query);

	List<String> selectSymbols();

	void insertStockAyalyseResult(StockAnalyseResult stockAnalyseResult);
	
	void updateStatus(Map<String,Object> map);

	List<String> selectAll();

	List<StockAnalyseResult> select1(String symbol);

	List<StockTop100> selectTop100(Date day);

	List<StockTop100> selectTop100Dl(Map<String, Object> createMap);

	Map<String, Date> selectDays(Date day);

	List<String> selectAllDay(int count);

	List<StockMainAnalyse> selectAnalyse(String day);

	void insertStockMainAnalyse(Map<String, Object> createMap);

	void updateStockMainDays(Map<String, Object> createMap);

	List<StockMainAnalyse> findStock();

	void insertAnalyse(Map<String, Object> createMap);

	StockAnalyseBase selectStockAnalyse(Map<String, Object> createMap);

	void insertStockAnaylseBase(Map<String, Object> map);

	List<CacheItem> initPrePrices();
	
	List<String> selectAllCodes();

	void insertStockBuySell(Map<String, Object> createMap);

	void insertFBVolume(Map<String, Object> createMap);
}