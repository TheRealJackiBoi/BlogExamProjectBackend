package dat3.config;

import dat3.dao.impl.PostDao;
import dat3.dao.impl.UserDao;
import dat3.model.Post;
import dat3.model.Role;
import dat3.model.User;
import dat3.model.Visibility;

public class Populate {


    public static void main(String[] args) {

        UserDao userDao = UserDao.getInstance(HibernateConfig.getEntityManagerFactory());
        PostDao postDao = PostDao.getInstance(HibernateConfig.getEntityManagerFactory());

        // Create users

        User user1 = new User("jslam@oulund.dk", "hej1234");
        User user2 = new User("julius@lassen.dk", "hej1234");
        User user3 = new User("bjarke@bjarke.dk", "hej1234");

        Role role1 = userDao.createRole("user");
        Role role2 = userDao.createRole("admin");
        Role role3 = userDao.createRole("manager");

        user3.addRole(role2);
        user3.addRole(role1);
        user2.addRole(role1);
        user2.addRole(role2);
        user1.addRole(role1);
        user1.addRole(role2);

        // Add users to database

        userDao.create(user1);
        userDao.create(user2);
        userDao.create(user3);

        // Create posts

        Post post1 = new Post("First Post", "This is my first post", Visibility.PUBLIC);
        Post post2 = new Post("Second Post", "This is my second post", Visibility.PUBLIC);
        Post post3 = new Post("Third Post", "This is my third post", Visibility.PUBLIC);
        Post post4 = new Post("Julius Post", "This is my post", Visibility.PUBLIC);

        // Add posts to users

        post1.setUser(user1);
        post2.setUser(user1);
        post3.setUser(user1);
        post4.setUser(user2);

        // Add posts to database

        post1 = postDao.create(post1);
        post2 = postDao.create(post2);
        post3 = postDao.create(post3);
        post4 = postDao.create(post4);




    }

}
