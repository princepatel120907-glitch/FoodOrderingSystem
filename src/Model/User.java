package Model;



import java.sql.Timestamp;

    public class User {
        private int id;
        private String name;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String securityQuestion;
        private String securityAnswer;
        private String role; // CUSTOMER or ADMIN
        private boolean isBlocked;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        // Default Constructor
        public User() {}

        // Parameterized Constructor
        public User(int id, String name, String email, String password, String phone,
                    String address, String securityQuestion, String securityAnswer,
                    String role, boolean isBlocked, Timestamp createdAt, Timestamp updatedAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.securityQuestion = securityQuestion;
            this.securityAnswer = securityAnswer;
            this.role = role;
            this.isBlocked = isBlocked;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getSecurityQuestion() { return securityQuestion; }
        public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

        public String getSecurityAnswer() { return securityAnswer; }
        public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public boolean isBlocked() { return isBlocked; }
        public void setBlocked(boolean blocked) { isBlocked = blocked; }

        public Timestamp getCreatedAt() { return createdAt; }
        public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

        public Timestamp getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", address='" + address + '\'' +
                    ", role='" + role + '\'' +
                    ", isBlocked=" + isBlocked +
                    '}';
        }
    }

