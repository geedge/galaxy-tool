package com.zdjizhi.test;

import java.util.Date;
import java.util.List;

import com.zdjizhi.utils.DateUtil;
import com.zdjizhi.utils.DateUtils;
import com.zdjizhi.utils.TimeConstants;
import org.apache.log4j.Logger;
import org.junit.Test;



public class DateUtilTest {
	Logger logger  = Logger.getLogger(DateUtilTest.class);
	
	@Test
	public void testGetCurrentDate(){
		logger.info(DateUtil.getCurrentDate());
		logger.info(DateUtils.getCurrentDate());
		logger.info(DateUtil.getCurrentDate(TimeConstants.YYYY_MM_DD_HH24_MM_SS));
		logger.info(DateUtils.getCurrentDate(TimeConstants.YYYY_MM_DD_HH24_MM_SS));
		
		
	}
	
	@Test
	public void testGetSomeday(){
		logger.info(DateUtil.getSomeDate("yyyy-MM-dd", 2));
        logger.info(DateUtils.getSomeDate("yyyy-MM-dd", 2));
		logger.info(DateUtil.getSomeDate("yyyy-MM-dd", -2));
        logger.info(DateUtils.getSomeDate("yyyy-MM-dd", -2));
		logger.info(DateUtil.getSomeDate("yyyy-MM-dd", 0));
        logger.info(DateUtils.getSomeDate("yyyy-MM-dd", 0));
	}
	@Test
	public void testGetFormatDate(){
		logger.info(DateUtil.getDateOfYesterday(DateUtil.YYYY_MM_DD_HH24_MM_SS));
        logger.info(DateUtils.getDateOfYesterday(DateUtil.YYYY_MM_DD_HH24_MM_SS));


        logger.info(DateUtil.getFormatDate( new Date(),DateUtil.YYYYMMDDHH24MMSS));
        logger.info(DateUtils.getFormatDate( new Date(),DateUtil.YYYYMMDDHH24MMSS));
	}
	@Test
	public void testGetDateAsStringToTimestamp(){
		logger.info(DateUtil.convertStringToTimestamp( DateUtil.getCurrentDate(TimeConstants.YYYY_MM_DD_HH24_MM_SS),DateUtil.YYYY_MM_DD_HH24_MM_SS));


		logger.info(DateUtils.convertStringToTimestamp( DateUtils.getCurrentDate(TimeConstants.YYYY_MM_DD_HH24_MM_SS), DateUtils.YYYY_MM_DD_HH24_MM_SS));
			
		
		
	}


	@Test
	public void testConvertTimestampToString() {
		logger.info(DateUtil.convertTimestampToString( 1536710400,DateUtil.YYYY_MM_DD_HH24_MM_SS));


		logger.info(DateUtils.convertTimestampToString( 1536710400,DateUtils.YYYY_MM_DD_HH24_MM_SS));



	}



	
	@Test
	public void testGetDateAsStringToDate(){
		try {
			logger.info(DateUtil.convertStringToDate( "2010-09-11 11:12:23",DateUtil.YYYY_MM_DD_HH24_MM_SS));

            logger.info(DateUtils.convertStringToDate( "2010-09-11 11:12:23",DateUtil.YYYY_MM_DD_HH24_MM_SS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	
	@Test
	public void testGetDateList(){
		try {
			List<Date> list = DateUtil.getDateList(DateUtil.convertStringToDate("2018-09-01",
					DateUtil.YYYY_MM_DD),new Date());
		
			for(Date date:list){
				logger.info(DateUtil.getFormatDate(date,DateUtil.YYYY_MM_DD ));
			}


            List<Date> lists = DateUtils.getDateList(DateUtil.convertStringToDate("2018-09-01",
                    DateUtil.YYYY_MM_DD),new Date());

            for(Date date:lists){
                logger.info(DateUtils.getFormatDate(date,DateUtils.YYYY_MM_DD ));
            }

        } catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetDateListForWorkDay(){
		try {
			List<Date> list = DateUtil.getWorkingdayList(DateUtil.convertStringToDate("2018-09-01",DateUtil.YYYY_MM_DD
					),new Date());


			for(Date date:list){
				logger.info(DateUtil.getFormatDate(date,DateUtil.YYYY_MM_DD));
			}


            List<Date> lists = DateUtils.getWorkingdayList(DateUtil.convertStringToDate("2018-09-01",DateUtil.YYYY_MM_DD
            ),new Date());


            for(Date date:lists){
                logger.info(DateUtil.getFormatDate(date,DateUtil.YYYY_MM_DD));
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void testGetFirstDayOfMonth(){
		try {
			logger.info(DateUtil.getFirstDayOfMonth(new Date(),DateUtil.YYYY_MM_DD_HH24_MM_SS ));
            logger.info(DateUtils.getFirstDayOfMonth(new Date(),DateUtil.YYYY_MM_DD_HH24_MM_SS ));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetLastDayOfMonth(){
		try {
			logger.info(DateUtil.getLastDayOfMonth(new Date(), DateUtil.YYYY_MM_DD_HH24_MM_SS));
            logger.info(DateUtils.getLastDayOfMonth(new Date(), DateUtil.YYYY_MM_DD_HH24_MM_SS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	public void testGetDayFromDate(){
		try {
			logger.info(DateUtil.getDDFromDate(new Date()));
            logger.info(DateUtils.getDDFromDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	public void testGetMonthFromDate(){
		try {
			logger.info(DateUtil.getMMFromDate(new Date()));
            logger.info(DateUtils.getMMFromDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	public void testGetYearFromDate(){
		try {
			logger.info(DateUtil.getYYFromDate(new Date()));
            logger.info(DateUtils.getYYFromDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	


}
