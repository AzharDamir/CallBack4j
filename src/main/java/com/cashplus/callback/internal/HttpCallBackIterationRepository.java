package com.cashplus.callback.internal;

import com.cashplus.callback.internal.HttpCallbackAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpCallBackIterationRepository extends JpaRepository<HttpCallbackAttempt, Long> {

}
