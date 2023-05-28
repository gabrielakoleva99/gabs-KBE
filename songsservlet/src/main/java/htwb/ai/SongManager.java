package htwb.ai;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;

import java.util.List;

public class SongManager {

    private EntityManagerFactory emf;
    private int counter = 0;


    public SongManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Integer saveSong(Song song) throws PersistenceException {
        EntityManager em = null;
        EntityTransaction transaction;
        try{
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(song);
            transaction.commit();
            return song.getId();

        }catch (IllegalStateException | EntityExistsException | RollbackException ex){
            if (em != null){
                em.getTransaction().rollback();
            }
            throw new PersistenceException(ex.getMessage());
        } finally {
            if (em != null){
                em.close();
            }
        }
    }

    public void saveSongList(List<Song> songs){
        for(Song song:songs){
            saveSong(song);
        }
    }

    public List<Song> findAllSongs(){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            Query q = em.createQuery("Select u FROM Song u");
            System.out.println("Songs found");
            return (List<Song>) q.getResultList();
        } finally {
            if (em != null){
                em.close();
            }
        }
    }

    public Song findSong(int id){
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            System.out.println("Song found");

            return em.find(Song.class, id);
        } finally {
            if (em != null){
                em.close();
            }
        }
    }

    public Song findSong(String id){
        return findSong(Integer.parseInt(id));
    }
}
