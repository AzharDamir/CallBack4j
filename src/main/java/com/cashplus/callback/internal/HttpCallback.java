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
public class HttpCallback extends CallBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String callBackData;
    @Convert(converter = URIConverter.class)
    private URI urlCallback;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "httpCallback")
    private List<HttpCallbackAttempt> httpCallbackAttempts =new ArrayList<>();//tentative

    @Embedded
    private HttpConfigurationCallBack configuration;

}
