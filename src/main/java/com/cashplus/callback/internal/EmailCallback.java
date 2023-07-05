package com.cashplus.callback.internal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class EmailCallback extends CallBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //private String messageBody;
    private String subject;
    private String senderEmail;
    private String recipientEmail;
}
