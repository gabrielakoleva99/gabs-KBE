package htwb.ai.modell;

import jakarta.persistence.*;

import java.util.List;


@Entity
    @Table(name = "usertable")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String userId;

        private String firstName;

        private String lastName;

        private String password;

    private int id;


    private String token;


        @OneToMany(mappedBy = "ownerId", orphanRemoval=true, fetch = FetchType.EAGER)
        private List<SongList> lists;


        public User() {
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

        public User(int id, String userId,String password, String firstName, String lastName, String token, List<SongList> lists) {
            this.id=id;
            this.userId = userId;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.token = token;
            this.lists = lists;
        }

//        public long getId() {
//            return id;
//        }

        public String getUserId() {
            return userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    public List<SongList> getLists() {
        if(lists == null) {
            System.out.println("Empty List");
            //lists = new List<>();
        }
        return lists;
    }
    public void addSongList(SongList list){
        if(lists==null){
            System.out.println("Empty List");
        } list.setOwnerId(this);
        this.lists.add(list);
    }
    public void setLists(List<SongList> lists) {
        this.lists = lists;
    }

        public String getFirstName() {
            return firstName;
        }

        public String setFirstName(String firstName) {
            return this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User  +  userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
                    + ", password=" + password + "]";
        }
    }
