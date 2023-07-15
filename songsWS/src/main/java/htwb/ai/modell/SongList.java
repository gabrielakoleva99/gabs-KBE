package htwb.ai.modell;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "SongList")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private User ownerId;

    private String name;

    private boolean isPrivate;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "songList_song",
            joinColumns = {@JoinColumn( name = "songlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn( name = "song_id", referencedColumnName = "id")})
    private List<Song> songs;

    public SongList(User ownerId, String name, boolean isPrivate, List<Song> songs) {
        this.ownerId = ownerId;
        this.name = name;
        this.isPrivate = isPrivate;
        this.songs = songs;
    }



    public SongList(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
//
//    public User getOwnerId() {
//        return ownerId;
//    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

//    public void addSong(Song song){
//        if(this.songs==null){
//            System.out.println("Empty SongsList");
//        }
//        song.getSongLists().add(this);
//        songs.add(song);
//    }

}