package com.shop.snackshop.service;

import com.shop.snackshop.dto.ReplyDTO;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.Reply;


import java.util.List;





public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    List<ReplyDTO> getList(Long id);
    //impl에 있음

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    default Reply dtoToEntity(ReplyDTO replyDTO) {

        Item item = Item.builder()
                .id(replyDTO.getId())
                .build();

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .item(item)
                .build();
        return reply;
    }

    default ReplyDTO entityToDTO(Reply reply) {

        ReplyDTO dto = ReplyDTO.builder()   //ReplyDTO를 빌드한다.
                .rno(reply.getRno())        //들어온 값으로 rno 부터 시간까지 입력 그값을 dto로 반환
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegTime())
                .modDate(reply.getUpdateTime())
                .build();

        return dto;
    }

}
