package com.mercadolivre.desafio_quality.service;

import com.mercadolivre.desafio_quality.model.District;
import com.mercadolivre.desafio_quality.model.Ground;
import com.mercadolivre.desafio_quality.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DistrictService {

    private final DistrictRepository repository;

    public DistrictService(DistrictRepository repository) {
        this.repository = repository;
    }

    /**
     * Salva um bairro
     * @param district Um objeto District que contem os dados do bairro
     * @return O objeto District que foi salvo
     */
    public District save (District district) {
        return  repository.save(district);
    }
}
