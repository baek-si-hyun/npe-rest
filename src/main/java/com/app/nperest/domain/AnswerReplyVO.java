package com.app.nperest.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@ToString(callSuper = true)
public class AnswerReplyVO extends Period implements Serializable {
    private Long id;
    private String replayContent;
    private boolean status;
    private Long memberId;
    private Long answerId;
}
