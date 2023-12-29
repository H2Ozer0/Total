package server;

import dao.LikeDAO;
import entity.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServer {

    private final LikeDAO likeDAO;

    @Autowired
    public LikeServer(LikeDAO likeDAO) {
        this.likeDAO = likeDAO;
    }

    public void insertLike(Like like) {
        likeDAO.insertLike(like);
    }

    public int getLikeCountByAlbum(int albumID) {
        return likeDAO.getLikeCountByAlbum(albumID);
    }
}
