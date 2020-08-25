package com.example.jd185150.efficenza20;

public class hrac_struct {
    String name;
    String post;
    String goly, asistence, body;
    String cislo;
    int ck, zk;

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public int getCk() {
        return ck;
    }

    public void setCk(int ck) {
        this.ck = ck;
    }

    public int getZk() {
        return zk;
    }

    public void setZk(int zk) {
        this.zk = zk;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getGoly() {
        return Integer.parseInt(goly);
    }

    public void setGoly(String goly) {
        this.goly = goly;
    }

    public int getAsistence() {
        return Integer.parseInt(asistence);
    }

    public void setAsistence(String asistence) {
        this.asistence = asistence;
    }

    public int getBody() {
        return Integer.parseInt(body);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public hrac_struct() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
