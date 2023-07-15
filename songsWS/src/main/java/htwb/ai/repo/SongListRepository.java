package htwb.ai.repo;

import htwb.ai.modell.SongList;
import htwb.ai.modell.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SongListRepository extends CrudRepository<SongList, Long> {
    @Query(value = "SELECT * FROM SongList WHERE id = ?1", nativeQuery = true)
    List<SongList> selectSongListById(int id);

    @Query(value = "SELECT * FROM SongList WHERE isPrivate = ?1 and ownerid = ?2", nativeQuery = true)
    List<SongList> findByIsPrivateAndUserId(boolean isPrivate, String ownerid);

    @Query(value = "SELECT * FROM SongList WHERE id = ?1 and ownerid = ?2", nativeQuery = true)
    List<SongList> findByIdAndOwnerId(int id, String ownerid);

    @Query(value = "SELECT * FROM SongList WHERE ownerid = ?1", nativeQuery = true)
    List<SongList> selectSongListByUser(String userId);

    @Query(value = "SELECT * FROM SongList WHERE isPrivate = ?1", nativeQuery = true)
    List<SongList> findByIsPrivate(boolean isPrivate);

    @Query(value = "SELECT * FROM SongList WHERE isPrivate = ?1 and ownerid = ?2 and id = ?3", nativeQuery = true)
    List<SongList> findByIsPrivateAndUserIdAndId(boolean isPrivate, String ownerid, int id);

    @Query(value = "SELECT * FROM SongList WHERE id = ?1 and ownerid = ?2", nativeQuery = true)
    SongList findSongListByIdAndUserId(int id, String ownerid);

}
