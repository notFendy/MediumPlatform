package uz.pdp.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import uz.pdp.model.Comment;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommentDao {
    private final SessionFactory session;


    public Comment save(Comment comment){

         session.getCurrentSession()
                .persist(comment);
         return comment;

    }

    public List<Comment> getCommentsByPostId(Long postId){
        return session.getCurrentSession()
                .createNativeQuery("select * from comment where post_id = ?1", Comment.class)
                .setParameter(1, postId)
                .list();

    }

}
