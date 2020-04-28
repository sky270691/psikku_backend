package com.psikku.backend.entity;

import java.util.List;

public class ExtractedToken {
    private long exp;
    private String user_name;
    private List<String> authorities;
    private String jti;
    private String client_id;
    private List<String> scope;

    public ExtractedToken(long exp, String user_name, List<String> authorities, String jti, String client_id, List<String> scope) {
        this.exp = exp;
        this.user_name = user_name;
        this.authorities = authorities;
        this.jti = jti;
        this.client_id = client_id;
        this.scope = scope;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "ExtractedToken{" +
                "exp=" + exp +
                ", user_name='" + user_name + '\'' +
                ", authorities=" + authorities +
                ", jti='" + jti + '\'' +
                ", client_id='" + client_id + '\'' +
                ", scope=" + scope +
                '}';
    }
}