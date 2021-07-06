package org.darkghast.fms.entity;

import org.apache.struts2.json.annotations.JSON;

public class User {
    /**
     * 用户的ID，作为唯一标识符
     */
    private Integer id;
    /**
     * 用户的邮箱
     */
    private String email;
    /**
     * 用户的用户名
     */
    private String username;
    /**
     * 用户的密码
     */
    private String password;

    /**
     * 带参数的构造方法
     *
     * @param id       用户ID
     * @param email    用户的邮箱
     * @param username 用户的用户名
     * @param password 用户的密码
     */
    public User(Integer id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JSON(serialize = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
