package com.hjun.timereport.global.job.twoweeks;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {

    private final JpaItemWriter<T> jpaItemWriter;

	@Override
    @Transactional
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, maxDelay = 2000))
    public void write(List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        items.forEach(totalList::addAll);

        jpaItemWriter.write(totalList);
    }


    @Override
    public void afterPropertiesSet(){
        // An EntityManagerFactory is required 방지
    }
}
