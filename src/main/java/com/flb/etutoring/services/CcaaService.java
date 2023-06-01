package com.flb.etutoring.services;

import java.util.List;

import com.flb.etutoring.models.Ccaa;

public interface CcaaService {
    public List<Ccaa> findAll();

    public Ccaa findById(int id);
}
