package org.mazhuang.guanggoo.data.entity;

import org.mazhuang.guanggoo.data.task.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mazhuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoteCommentResponse extends BaseResponse {

    private boolean voted;

    private int count;

    private boolean changed;

}
