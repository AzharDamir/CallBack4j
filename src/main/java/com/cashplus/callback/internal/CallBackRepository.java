package com.cashplus.callback.internal;


import com.cashplus.callback.internal.HttpCallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallBackRepository extends JpaRepository<CallBack, Long> {
    long countByServiceNameAndRequestId(String serviceName, int requestId);
    CallBack findByServiceNameAndRequestId(String serviceName, int requestId);
}
