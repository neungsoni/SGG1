package com.shop.snackshop.repository;

import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    // Item 삭제시 댓글들 삭제
    List<Reply> getRepliesByItemOrderByRno(Item item);

    //item 삭제시 댓글들 삭제
    /*@Modifying
    @Query("delete from Reply r where r.item.id = :id ")
    void deleteById(Long id);
*/
}
