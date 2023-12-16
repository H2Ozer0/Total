package dao;

import controllers.Album_Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APPConfig {


    @Bean
    public AlbumDAO albumDAO() {
        return new AlbumDAO();
    }
    @Bean
    public FriendshipDAO friendshipDAO() {
        return new FriendshipDAO();
    }
    @Bean
    public UserDAO UserDao()
    {
        return new UserDAO();
    }

}