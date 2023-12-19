package dao;

import controllers.Album_Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.*;

@Configuration
public class APPConfig {


    @Bean
    public AlbumDAO albumDAO() {
        return new AlbumDAO();
    }
    @Bean
    public CommentDAO commentDAO(){return new CommentDAO();}
    @Bean
    public FavoriteDAO favoriteDAO(){return new FavoriteDAO();}
    @Bean
    public FriendshipDAO friendshipDAO() {
        return new FriendshipDAO();
    }
    @Bean
    public LikeDAO likeDAO(){return new LikeDAO();}
    @Bean
    public PhotoDAO photoDAO(){return new PhotoDAO();}
    @Bean
    public UserDAO UserDao()
    {
        return new UserDAO();
    }

    @Bean
    public AlbumServer albumServer(){return new AlbumServer(albumDAO());}
    @Bean
    public UserServer UserServer(){return new UserServer();}

}