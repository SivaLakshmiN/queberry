package io.queberry.que.Entity;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class EmployeeData {
    private String firstname;
    private String middlename;
    private String lastname;
    private String username;
    private String password;
    private boolean active;

    private boolean callByNumber;
    private boolean enableAutoCall;
    private boolean forceAutoCall;
    private boolean callAll;
    private boolean callNew;
    private boolean callTransfer;
    private boolean walkIn;
    private boolean break_btn;
    private boolean park;
    private boolean transferUser;
    private boolean transferCounter;
    private boolean transferService;
    private boolean showServiceList;

    private String region;
    private List<String> roles;
    private List<String> branches;
    private List<String> services;
    private Set<String> second;
    private Set<String> third;
    private Set<String> fourth;

}
