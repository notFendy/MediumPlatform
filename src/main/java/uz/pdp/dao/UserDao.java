package uz.pdp.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import uz.pdp.domain.User;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserDao {

    private final SessionFactory session;

    public User getUserByUsername(final String username) {
        return session.getCurrentSession()
//                .createQuery("select u from User u where u.name = ?1", User.class)
                .createNativeQuery("select * from users where username = ?1", User.class)
                .setParameter(1, username)
                .uniqueResult();
    }

    public User save(User user) {
        session.getCurrentSession()
                .persist(user);
        return user;
    }
    public User getByEmail(final String email) {
       return session.getCurrentSession()
                .createNativeQuery("select * from users where email = ?1", User.class)
               .setParameter(1, email)
               .uniqueResult();

    }

    public User update(User user){
        return session.getCurrentSession()
                .merge(user);
    }


    public User getById(long id){
        return session.getCurrentSession()
                .get(User.class, id);
    }

    public void delete(long id){
        session.getCurrentSession()
                .remove(getById(id));
    }

    public List<User> getAll(){
        return session.getCurrentSession()
                .createQuery("from User", User.class)
                .list();
    }
}
