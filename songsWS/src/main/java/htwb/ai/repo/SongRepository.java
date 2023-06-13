package htwb.ai.repo;

import java.util.List;

import htwb.ai.modell.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SongRepository extends CrudRepository<Song, Long> {

//    public Song findById(int songId);
}
