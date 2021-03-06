package com.stock.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.stock.dao.ExceptionLogMapper;
import com.stock.dao.HolidayMapper;
import com.stock.model.ExceptionLog;
import com.stock.model.StockConstant;
import com.stock.service.InitStockServiceI;
import com.stock.service.StockMainServiceI;
import com.stock.util.CommonsUtil;

public class TaskJob {

	private InitStockServiceI initStockServiceI;
	private ExceptionLogMapper exceptionLogMapper;
	private HolidayMapper holidayMapper;
	private StockMainServiceI stockMainServiceI;
	private Logger log = Logger.getLogger(TaskJob.class);

	@Autowired
	public void setStockMainServiceI(StockMainServiceI stockMainServiceI) {
		this.stockMainServiceI = stockMainServiceI;
	}

	@Autowired
	public void setHolidayMapper(HolidayMapper holidayMapper) {
		this.holidayMapper = holidayMapper;
	}

	@Autowired
	public void setInitStockServiceI(InitStockServiceI initStockServiceI) {
		this.initStockServiceI = initStockServiceI;
	}

	@Autowired
	public void setExceptionLogMapper(ExceptionLogMapper exceptionLogMapper) {
		this.exceptionLogMapper = exceptionLogMapper;
	}

	public void execute() {
		try {
			boolean run = true;
			int i = 20;
			while (run) {
				run = this.stockMainServiceI.analyse1(i);
			}
		} catch (Exception e) {
			log.info(CommonsUtil.join(e.getStackTrace(), ","));
			ExceptionLog record = new ExceptionLog(
					CommonsUtil.formatDateToString3(new Date()), this
							.getClass().getName(), "execute", e.getMessage(),
					CommonsUtil.join(e.getStackTrace(), ","));
			this.exceptionLogMapper.insert(record);
		}
	}

	/**
	 * 下载股票详情，所有A股每天每隔两分钟刷新一次的数据
	 */
	public void downLoad1() {
		try {
			// 如果当前不在股市的交易时间
			while (CommonsUtil.checkTime(this.holidayMapper)) {
				log.info("开始下载每天股票详情。。。");
				long begin = System.currentTimeMillis();
				initStockServiceI.initStock();
				long host = System.currentTimeMillis() - begin;
				long sleep = StockConstant.INIT_STOCK_SLEEP_TIME - host;
				if (sleep > 0) {
					log.info("休眠时间是："+sleep);
					Thread.sleep(sleep);
				}
			}
			log.info("下载每天股票详情任务结束");
		} catch (Exception e) {
			log.info(CommonsUtil.join(e.getStackTrace(), ","));
			ExceptionLog record = new ExceptionLog(
					CommonsUtil.formatDateToString3(new Date()), this
							.getClass().getName(), "downLoad1", e.getMessage(),
					CommonsUtil.join(e.getStackTrace(), ","));
			this.exceptionLogMapper.insert(record);
		}
	}
	
	public void initStockEveryDay(){
		try {
			initStockServiceI.initStockEveryDay();
		} catch (Exception e) {
			log.info(CommonsUtil.join(e.getStackTrace(), ","));
			ExceptionLog record = new ExceptionLog(
					CommonsUtil.formatDateToString3(new Date()), this
							.getClass().getName(), "initStockEveryDay", e.getMessage(),
					CommonsUtil.join(e.getStackTrace(), ","));
			this.exceptionLogMapper.insert(record);
		}
	}

	public void initBuyAndSell(){
		long time = 20000;
		while(CommonsUtil.checkTime(holidayMapper)){
			long begin = System.currentTimeMillis();
			initStockServiceI.initBuyAndSell();
			long remain = System.currentTimeMillis() - begin;
			long sleep = time-remain;
			if(sleep>0){
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					log.info(CommonsUtil.join(e.getStackTrace(), ","));
					ExceptionLog record = new ExceptionLog(
							CommonsUtil.formatDateToString3(new Date()), this
									.getClass().getName(), "initBuyAndSell", e.getMessage(),
							CommonsUtil.join(e.getStackTrace(), ","));
					this.exceptionLogMapper.insert(record);
				}
			}
		}
	}
	
	/**
	 * 下载成交量
	 */
	public void initCJL(){
		try {
			initStockServiceI.insertCJL();
		} catch (Exception e) {
			log.info(CommonsUtil.join(e.getStackTrace(), ","));
			ExceptionLog record = new ExceptionLog(
					CommonsUtil.formatDateToString3(new Date()), this
							.getClass().getName(), "initCJL", e.getMessage(),
					CommonsUtil.join(e.getStackTrace(), ","));
			this.exceptionLogMapper.insert(record);
		}
	}
	
	/**
	 * 将excel格式的成交量导入到数据库中，每天执行一次
	 */
	public void insertCJL(){
		try {
			while(CommonsUtil.checkTime(holidayMapper)){
				initStockServiceI.insertCJL();
			}
		} catch (Exception e) {
			log.info(CommonsUtil.join(e.getStackTrace(), ","));
			ExceptionLog record = new ExceptionLog(
					CommonsUtil.formatDateToString3(new Date()), this
							.getClass().getName(), "insertCJL", e.getMessage(),
					CommonsUtil.join(e.getStackTrace(), ","));
			this.exceptionLogMapper.insert(record);
		}
	}
}
