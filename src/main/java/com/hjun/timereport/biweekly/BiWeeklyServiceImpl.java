package com.hjun.timereport.biweekly;

import static com.hjun.timereport.global.response.BaseResponseStatus.INSERT_ERROR_TWO_WEEKS;
import static com.hjun.timereport.global.response.BaseResponseStatus.INVALID_DATE_FORMAT;
import static com.hjun.timereport.global.response.BaseResponseStatus.NOT_EXIST_HOLIDAY;
import static com.hjun.timereport.global.response.BaseResponseStatus.NOT_EXIST_TWO_WEEKS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.biweekly.dto.HolidayDto;
import com.hjun.timereport.biweekly.dto.TwoWeeksBoxDto;
import com.hjun.timereport.biweekly.dto.TwoWeeksDto;
import com.hjun.timereport.biweekly.entity.Holiday;
import com.hjun.timereport.biweekly.entity.TwoWeeks;
import com.hjun.timereport.biweekly.repository.HolidayQueryRepository;
import com.hjun.timereport.biweekly.repository.HolidayRepository;
import com.hjun.timereport.biweekly.repository.TwoWeeksRepository;
import com.hjun.timereport.global.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BiWeeklyServiceImpl implements BiWeeklyService {

	private final HolidayRepository holidayRepository;
	private final HolidayQueryRepository holidayQueryRepository;
	private final TwoWeeksRepository twoWeeksRepository;

	public BiWeeklyServiceImpl(HolidayRepository holidayRepository, HolidayQueryRepository holidayQueryRepository,
			TwoWeeksRepository twoWeeksRepository) {
		this.holidayRepository = holidayRepository;
		this.holidayQueryRepository = holidayQueryRepository;
		this.twoWeeksRepository = twoWeeksRepository;
	}

	@Override
	public Map<String, String> findHolidayBetweenMap(String startDay, String endDay) {
		return holidayQueryRepository.findHolidayBetween(startDay, endDay);
	}

	@Override
	public Map<String, String> findHolidayInMap(List<String> days) {
		return holidayQueryRepository.findHolidayIn(days);
	}


	@Override
	public List<HolidayDto> findHolidayDateBetweenList(String startDay, String endDay) {
		return holidayRepository.findByHolidayDateBetween(startDay, endDay).stream()
					.map(HolidayDto::entityToDto)
					.collect(Collectors.toList());
	}

	@Override
	public HolidayDto findHolidayBy(String holidayDate) {
		Holiday findHoliday = holidayRepository.findByHolidayDate(holidayDate)
				.orElseThrow(() -> new BaseException(NOT_EXIST_HOLIDAY));

		return HolidayDto.entityToDto(findHoliday);
	}

	@Override
	public String findStartOfWeekday(String targetDay) {

		String targetYear = targetDay.substring(0, 4);

		String lastYearWeek = twoWeeksRepository.findFirstByYearOrderByFromdtDesc(String.valueOf(Integer.parseInt(targetYear)-1))
				.orElseThrow(() -> new BaseException(NOT_EXIST_TWO_WEEKS)).getFromdt();

		String startOfWeekday = twoWeeksRepository.findStartOfWeekday(String.valueOf(Integer.parseInt(targetYear) - 1), targetYear, lastYearWeek, targetDay);

		return startOfWeekday;
	}

	@Override
	public TwoWeeksBoxDto findTwoWeeks() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String day = LocalDate.now().format(formatter);
		day = findStartOfWeekday(day);


		List<TwoWeeksDto> twoWeekDtos = twoWeeksRepository.findAll()
			.stream()
			.sorted(Comparator.comparing(TwoWeeks::getTodt))
			.map(TwoWeeks::entityToDto)
			.collect(Collectors.toList());

		return TwoWeeksBoxDto.builder()
			.twoWeeksDtos(twoWeekDtos)
			.startOfWeek(day)
			.build();
	}

	//승인 휴일 체크
	@Override
	public Holiday findByHolidayDate(String holidayDate){
		Holiday holiday = holidayRepository.findByHolidayDate(holidayDate)
				.orElseThrow(() -> new BaseException(NOT_EXIST_HOLIDAY));
		return holiday;

	}

	@Override
	public boolean isExistFromdt(String targetDay) {
		return twoWeeksRepository.existsByFromdt(targetDay);
	}


	@Override
	@Transactional
	public Boolean saveTwoWeeks() {

		// Native Query 이용하여 가장 최근 날짜를 가져옴
		TwoWeeks lastTwoWeeks = twoWeeksRepository.findLastTwoWeeks();

		log.info("가져온 최근 날짜 : {}", lastTwoWeeks.getFromdt());

		// 날짜가 존재하는 경우 -> 넘어감
		if (isExistFromdt(lastTwoWeeks.getFromdt())) {
			log.info("현재 존재하는 주 시작일 => {}", lastTwoWeeks.getFromdt());
			return true;
		}

		// 없는 경우 등록
		try {
			TwoWeeks toSaveTwoWeeks = TwoWeeks.builder()
				.year(lastTwoWeeks.getYear())
				.yyyymmww(lastTwoWeeks.getYyyymmww())
				.content(lastTwoWeeks.getContent())
				.fromdt(lastTwoWeeks.getFromdt())
				.month(lastTwoWeeks.getMonth())
				.todt(lastTwoWeeks.getTodt())
				.yyyyww(lastTwoWeeks.getYyyyww())
				.build();

			log.info("등록할 새로운 주 시작일 => {}", lastTwoWeeks.getFromdt());
			twoWeeksRepository.save(toSaveTwoWeeks);

		} catch (Exception e) {
			throw new BaseException(INSERT_ERROR_TWO_WEEKS);
		}

		return true;
	}

	@Override
	public boolean isValidYearAndMonth(String year, String month) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		// 1. size check
		if (year.length() != 4 || month.length() != 2)
			throw new BaseException(INVALID_DATE_FORMAT);

		// 2. 숫자가 맞는지 확인
		try {
			Double.parseDouble(year);
			Double.parseDouble(month);
		} catch (NumberFormatException nfe) {
			throw new BaseException(INVALID_DATE_FORMAT);
		}

		// 3. 날짜 형식이 맞는지 확인
        try {
			LocalDate.parse(year+month+"01", formatter);
        } catch (DateTimeParseException e) {
            throw new BaseException(INVALID_DATE_FORMAT);
        }

		return true;
	}

	@Override
	public boolean isValidDate(String targetDay) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            LocalDate.parse(targetDay, formatter);
        } catch (DateTimeParseException e) {
            throw new BaseException(INVALID_DATE_FORMAT);
        }

	    return true;
	}

	@Override
	public List<Holiday> findHolidayDateBetween(String startDay, String endDay) {
		return holidayRepository.findByHolidayDateBetween(startDay, endDay);
	}

}
