package com.cashplus.callback.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
class HttpCallbackAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sending_date", updatable = false)
    private Date sendingDate;
    @Lob
    private String responseBody;
    private int responseStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseDate;
    @ManyToOne
    @JoinColumn(name="httpCallback_id", nullable=false)
    @JsonIgnore
    private HttpCallback httpCallback;
}
