package com.techgear.pago.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techgear.pago.model.Fpago;
import com.techgear.pago.repository.FpagoRepository;

@Service
public class FpagoService {

    @Autowired
    private FpagoRepository fpagoRepository;

    public List<Fpago> getAllFpagos(){
        return fpagoRepository.findAll();
    }

    public Fpago getFpago(int id){
        return fpagoRepository.findById(id).orElse(null);
    }

    public Fpago saveFpago(Fpago fpago){
        return fpagoRepository.save(fpago);
    }

    public Fpago updateFpago(Fpago fpago){
        Fpago updFpago = getFpago(fpago.getId());
        if (updFpago==null) {
            return null;
        }
        updFpago.setNombreFpago(fpago.getNombreFpago());
        
        return fpagoRepository.save(updFpago);
    }

    public void deleteFpago(Integer id){
        fpagoRepository.deleteById(id);
    }
}
