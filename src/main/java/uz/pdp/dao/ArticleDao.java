package uz.pdp.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import uz.pdp.model.Article;
import uz.pdp.model.User;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ArticleDao {
    private final SessionFactory session;
    private final UserDao userDao;
    public Article save(Article article, String username){
        article.setUser(userDao.getUserByUsername(username));
        session.getCurrentSession()
                .persist(article);
        return article;
    }

    public Article getById(Long id){
        return session.getCurrentSession()
                .get(Article.class, id);

    }
    public Article getByTitle(String title){
        return session.getCurrentSession()
                .createNativeQuery("select * from posts where title ilike '%' || ?1 || '%'", Article.class)
                .setParameter("1", title)
                .uniqueResult();
    }

    public List<Article> getPostsByUserId(long id){
        return session.getCurrentSession()
                .createNativeQuery("select content, title from posts where user_id = ?1", Article.class)
                .setParameter(1, id)
                .list();


    }
    public List<Article> getAllPosts(){

        return session.getCurrentSession()
                .createQuery("from Article", Article.class).list();
    }

    public Article findByUser(User currentUser) {
        return session.getCurrentSession()
                .get(Article.class, currentUser.getId());
    }
}
