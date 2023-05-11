package com.shop.snackshop.service;



import com.shop.snackshop.dto.ReplyDTO;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.Reply;
import com.shop.snackshop.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private  final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);
        return  reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long id) {
        //아이디를 파라미터값으로 받아서 getRepliesByItemOrderByRno 매서드를 실행
        List<Reply> result = replyRepository
                .getRepliesByItemOrderByRno(Item.builder()
                        .id(id)
                        .build());



        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }
}
