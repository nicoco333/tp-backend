package com.tpi.microservicioseguimiento.service;

import com.tpi.microservicioseguimiento.entity.Deposito;
import com.tpi.microservicioseguimiento.repository.DepositoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepositoService {

    private final DepositoRepository depositoRepository;
    
    @Autowired 
    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public Deposito registrarDeposito(Deposito deposito) {
        return depositoRepository.save(deposito);
    }
    
    public List<Deposito> listarTodos() {
        return depositoRepository.findAll();
    }
}