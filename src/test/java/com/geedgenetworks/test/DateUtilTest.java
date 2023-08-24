package com.geedgenetworks.test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.geedgenetworks.utils.DateUtils;
import com.geedgenetworks.utils.TimeConstants;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;



public class DateUtilTest {
	Logger logger  = Logger.getLogger(DateUtilTest.class);
	
	@Test
	public void testGetCurrentDate(){
		logger.info(DateUtils.getCurrentDate());
		logger.info(DateUtils.getCurrentDate(TimeConstants.YYYY_MM_DD_HH24_MM_SS));
		
		
	}
	
	@Test
	public void testGetSomeday(){
        logger.info(DateUtils.getSomeDate("yyyy-MM-dd", 2));
        logger.info(DateUtils.getSomeDate("yyyy-MM-dd", -2));
        logger.info(DateUtils.getSomeDate("yyyy-MM-dd", 0));
	}
	@Test
	public void testGetFormatDate(){
        logger.info(DateUtils.getDateOfYesterday(DateUtils.YYYY_MM_DD_HH24_MM_SS));


        logger.info(DateUtils.getFormatDate( new Date(),DateUtils.YYYYMMDDHH24MMSS));
	}
	@Test
	public void testGetDateAsStringToTimestamp(){

		logger.info(DateUtils.convertStringToTimestamp( DateUtils.getCurrentDate(TimeConstants.YYYY_MM_DD_HH24_MM_SS), DateUtils.YYYY_MM_DD_HH24_MM_SS));
			
		
		
	}


	@Test
	public void testConvertTimestampToString() {
		logger.info(DateUtils.convertTimestampToString( 1536710400,DateUtils.YYYY_MM_DD_HH24_MM_SS));



	}



	
	@Test
	public void testGetDateAsStringToDate(){
		try {

            logger.info(DateUtils.convertStringToDate( "2010-09-11 11:12:23",DateUtils.YYYY_MM_DD_HH24_MM_SS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	
	@Test
	public void testGetDateList(){
		try {


            List<Date> lists = DateUtils.getEveryDay(DateUtils.convertStringToDate("2018-09-01",
                    DateUtils.YYYY_MM_DD),new Date());

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

            List<Date> lists = DateUtils.getWorkingdayList(DateUtils.convertStringToDate("2018-09-01",DateUtils.YYYY_MM_DD
            ),new Date());


            for(Date date:lists){
                logger.info(DateUtils.getFormatDate(date,DateUtils.YYYY_MM_DD));
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void testGetFirstDayOfMonth(){
		try {
            logger.info(DateUtils.getFirstDayOfMonth(new Date(),DateUtils.YYYY_MM_DD_HH24_MM_SS ));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetLastDayOfMonth(){
		try {
            logger.info(DateUtils.getLastDayOfMonth(new Date(), DateUtils.YYYY_MM_DD_HH24_MM_SS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	public void testGetDayFromDate(){
		try {
            logger.info(DateUtils.getDDFromDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	public void testGetMonthFromDate(){
		try {
            logger.info(DateUtils.getMMFromDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	public void testGetYearFromDate(){
		try {
            logger.info(DateUtils.getYYFromDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Test
	public void testGetDateRange() {

		List<Date>lists = DateUtils.getDateRange(DateUtils.convertStringToDate("2020-06-01 01:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),
				DateUtils.convertStringToDate("2020-06-02 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),"PT3H");
		Assert.assertEquals(lists.size(), 9);
	}

	@Test
	public void testGetLeftDateRange() {

		List<Date>lists = DateUtils.getLeftDateRange(DateUtils.convertStringToDate("2020-06-02 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),
				DateUtils.convertStringToDate("2020-06-01 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),"PT3H");
		Assert.assertEquals(lists.size(), 8);
	}


	@Test
	public void testGetRightDateRange() {

		List<Date>lists = DateUtils.getRightDateRange(DateUtils.convertStringToDate("2020-06-02 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),
				DateUtils.convertStringToDate("2020-06-03 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),"PT3H");
		Assert.assertEquals(lists.size(), 9);
	}



	@Test
	public void testGetLeftAndRightDateRange() {
		List leftLists = DateUtils.getLeftDateRange(DateUtils.convertStringToDate("2020-06-01 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),
				DateUtils.convertStringToDate("2020-06-01 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),"PT3H");

		Collections.sort(leftLists);

		List rightLists = DateUtils.getRightDateRange(DateUtils.convertStringToDate("2020-06-02 00:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),
				DateUtils.convertStringToDate("2020-06-02 23:00:00", DateUtils.YYYY_MM_DD_HH24_MM_SS),"PT3H");


		List<Date> lists = Lists.newArrayList(Iterables.concat(leftLists, rightLists));

		Assert.assertEquals(lists.size(), 8);







	}


	
	


}
