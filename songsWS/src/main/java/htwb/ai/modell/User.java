package htwb.ai.modell;

import jakarta.persistence.*;


    @Entity
    @Table(name = "usertable")
    public class User {

//        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        private long id;

        @Id
        private String userId;

        private String firstName;

        private String lastName;

        private String password;

        private String token;


        public User() {
        }

        public User(String userId,String password, String firstName, String lastName, String token) {
            this.userId = userId;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.token = token;
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
