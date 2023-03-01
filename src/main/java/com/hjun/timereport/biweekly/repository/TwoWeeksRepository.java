package com.hjun.timereport.biweekly.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hjun.timereport.biweekly.entity.TwoWeeks;

public interface TwoWeeksRepository extends JpaRepository<TwoWeeks, Long> {

	@Query(value = "WITH t AS"
			+ " ("
			+ " SELECT TO_DATE(yyyy||'01', 'yyyymm') + LEVEL - 1 SDATE"
			+ " FROM (SELECT :start_year yyyy FROM dual)"
			+ " CONNECT BY LEVEL <= TO_CHAR(TO_DATE(yyyy||'1231', 'yyyymmdd'), 'ddd')"
			+ " UNION ALL "
			+ " SELECT TO_DATE(yyyy||'01', 'yyyymm') + LEVEL - 1 SDATE"
			+ " FROM (SELECT :end_year yyyy FROM dual)"
			+ " CONNECT BY LEVEL <= TO_CHAR(TO_DATE(yyyy||'1231', 'yyyymmdd'), 'ddd')"
			+ ")"
			+ " Select DISTINCT LAST_VALUE(\"dates\") OVER() AS \"dates\""
			+ " FROM ("
			+ " SELECT TO_CHAR(SDATE,'YYYYMMDD') as \"dates\""
			+ " ,ROWNUM AS RN"
			+ " FROM T"
//			+ " WHERE TO_CHAR(SDATE,'YYYYMMDD') BETWEEN :last_year_weekday AND TO_CHAR(SYSDATE,'yyyymmdd')"
			+ " WHERE TO_CHAR(SDATE,'YYYYMMDD') BETWEEN :last_year_weekday AND :target_day"
			+ " AND TO_CHAR(SDATE,'d') =  2"
			+ ") T1"
			+ " where MOD(T1.RN,2) = 1", nativeQuery = true)
	String findStartOfWeekday(@Param(value = "start_year") String startYear,
							 @Param(value="end_year") String endYear,
							 @Param(value="last_year_weekday") String lastYearWeekday,
							 @Param(value="target_day") String targetDay);


	@Query(value = "INSERT into TWO_WEEKS"
		+ " WITH t AS"
		+ "  ("
		+ "   SELECT TO_DATE(yyyy||'01', 'yyyymm') + LEVEL - 1 SDATE"
		+ "   FROM (SELECT (select TO_CHAR(sysdate - (interval '1' year), 'yyyy') from dual) yyyy FROM dual)"
		+ "   CONNECT BY LEVEL <= TO_CHAR(TO_DATE(yyyy||'1231', 'yyyymmdd'), 'ddd')"
		+ "   UNION ALL"
		+ "   SELECT TO_DATE(yyyy||'01', 'yyyymm') + LEVEL - 1 SDATE"
		+ "   FROM (SELECT (select TO_CHAR(sysdate, 'yyyy') from dual) yyyy FROM dual)"
		+ "   CONNECT BY LEVEL <= TO_CHAR(TO_DATE(yyyy||'1231', 'yyyymmdd'), 'ddd')"
		+ "  )"
		+ "  "
		+ "  Select TWO_WEEKS_SEQ.nextval AS id"
		+ "     , year"
		+ "     , month"
		+ "     , fromdt"
		+ "     , (SELECT TO_CHAR(NEXT_DAY(NEXT_DAY(TO_DATE(fromdt, 'YYYYMMDD'), '일'), '일'), 'yyyymmdd') FROM dual) AS todt"
		+ "     , YYYYWW"
		+ "     , content"
		+ "     , YYYYMMWW"
		+ "  FROM ("
		+ "    SELECT  TO_CHAR(SDATE, 'YYYY') AS year,"
		+ "            TO_CHAR(SDATE, 'MM') AS month,"
		+ "            (select TO_CHAR(SDATE, 'YYYY')||to_char(SDATE,'IW') from dual) AS YYYYWW,"
		+ "            TO_CHAR(SDATE, 'YYYYMM')||'0'||TO_CHAR(SDATE, 'W') as YYYYMMWW,"
		+ "            (SELECT TO_CHAR(SDATE, 'MM')||'월 '||TO_CHAR(SDATE, 'W')||'주 ~ '||TO_CHAR(SDATE+14, 'MM')||'월 '||TO_CHAR(SDATE+14, 'W')||'주' FROM dual) AS content,"
		+ "            TO_CHAR(SDATE,'YYYYMMDD') as fromdt,"
		+ "            ROWNUM AS RN"
		+ "    FROM T"
		+ "    WHERE TO_CHAR(SDATE,'YYYYMMDD') BETWEEN (select fromdt from (select * from two_weeks order by fromdt desc) where year=(select TO_CHAR(sysdate - (interval '1' year), 'yyyy') from dual) and rownum=1) AND TO_CHAR(SYSDATE,'yyyymmdd')"
		+ "      AND TO_CHAR(SDATE,'d') =  2"
		+ "    order by fromdt desc"
		+ "     ) T1"
		+ "  where MOD(T1.RN,2) = 1 and rownum=1", nativeQuery = true)
	void insertStartOfWeekday();

	@Query(value = " WITH t AS"
		+ "  ("
		+ "   SELECT TO_DATE(yyyy||'01', 'yyyymm') + LEVEL - 1 SDATE"
		+ "   FROM (SELECT (select TO_CHAR(sysdate - (interval '1' year), 'yyyy') from dual) yyyy FROM dual)"
		+ "   CONNECT BY LEVEL <= TO_CHAR(TO_DATE(yyyy||'1231', 'yyyymmdd'), 'ddd')"
		+ "   UNION ALL"
		+ "   SELECT TO_DATE(yyyy||'01', 'yyyymm') + LEVEL - 1 SDATE"
		+ "   FROM (SELECT (select TO_CHAR(sysdate, 'yyyy') from dual) yyyy FROM dual)"
		+ "   CONNECT BY LEVEL <= TO_CHAR(TO_DATE(yyyy||'1231', 'yyyymmdd'), 'ddd')"
		+ "  )"
		+ "  "
		+ "  Select TWO_WEEKS_SEQ.nextval AS id"
		+ "     , year"
		+ "     , month"
		+ "     , fromdt"
		+ "     , (SELECT TO_CHAR(NEXT_DAY(NEXT_DAY(TO_DATE(fromdt, 'YYYYMMDD'), '일'), '일'), 'yyyymmdd') FROM dual) AS todt"
		+ "     , YYYYWW"
		+ "     , content"
		+ "     , YYYYMMWW"
		+ "  FROM ("
		+ "    SELECT  TO_CHAR(SDATE, 'YYYY') AS year,"
		+ "            TO_CHAR(SDATE, 'MM') AS month,"
		+ "            (select TO_CHAR(SDATE, 'YYYY')||to_char(SDATE,'IW') from dual) AS YYYYWW,"
		+ "            TO_CHAR(SDATE, 'YYYYMM')||'0'||TO_CHAR(SDATE, 'W') as YYYYMMWW,"
		+ "            (SELECT TO_CHAR(SDATE, 'MM')||'월 '||TO_CHAR(SDATE, 'W')||'주 ~ '||TO_CHAR(SDATE+14, 'MM')||'월 '||TO_CHAR(SDATE+14, 'W')||'주' FROM dual) AS content,"
		+ "            TO_CHAR(SDATE,'YYYYMMDD') as fromdt,"
		+ "            ROWNUM AS RN"
		+ "    FROM T"
		+ "    WHERE TO_CHAR(SDATE,'YYYYMMDD') BETWEEN (select fromdt from (select * from two_weeks order by fromdt desc) where year=(select TO_CHAR(sysdate - (interval '1' year), 'yyyy') from dual) and rownum=1) AND TO_CHAR(SYSDATE+13,'yyyymmdd')"
		+ "      AND TO_CHAR(SDATE,'d') =  2"
		+ "    order by fromdt desc"
		+ "     ) T1"
		+ "  where MOD(T1.RN,2) = 1 and rownum=1", nativeQuery = true)
	TwoWeeks findLastTwoWeeks();

	Optional<TwoWeeks> findFirstByYearOrderByFromdtDesc(String year);

	boolean existsByFromdt(String targetDay);

	Optional<TwoWeeks> findByFromdt(String fromdt);
}
